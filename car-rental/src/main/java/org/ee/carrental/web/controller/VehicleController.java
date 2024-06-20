package org.ee.carrental.web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ee.carrental.web.dao.VehicleDao;
import org.ee.carrental.web.model.Vehicle;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

@WebServlet(name = "VehicleController", urlPatterns = {"/vehicle/list", "/vehicle/edit/*", "/vehicle/remove/*", "/vehicle/reserve/*"})
public class VehicleController extends HttpServlet {

    @EJB
    private VehicleDao vehicleDao;
    
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();
        
        switch (path) {
            case "/vehicle/list":
                handleVehicleList(req, res);
                break;
            case "/vehicle/edit":
                if (req.getSession().getAttribute("userGroups") != null && ((List<String>) req.getSession().getAttribute("userGroups")).contains("ROLE_ADMIN")) {
                    handleVehicleEditGet(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                break;
            case "/vehicle/remove":
                if (req.getSession().getAttribute("userGroups") != null && ((List<String>) req.getSession().getAttribute("userGroups")).contains("ROLE_ADMIN")) {
                    handleVehicleRemove(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/vehicle/edit":
                if (req.getSession().getAttribute("userGroups") != null && ((List<String>) req.getSession().getAttribute("userGroups")).contains("ROLE_ADMIN")) {
                    handleVehicleEditPost(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                break;
            case "/vehicle/reserve":
                if (req.getSession().getAttribute("user") != null) {
                    handleVehicleReserve(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                break;
        }
    }

    private void handleVehicleList(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        // wczytuje listę pojazdów z bazy
        List<Vehicle> vehicles = vehicleDao.findAll();
        // ustawia listę pojazdów jako atrybut pod nazwą "vehicleList" dostępny na stronie jsp
        req.setAttribute("vehicleList", vehicles);
        // przekazuje sterowanie do strony jsp która renderuje listę pojazdów
        req.getRequestDispatcher("/WEB-INF/views/vehicle/vehicle_list.jsp").forward(req, res);
    }

    private void handleVehicleEditGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String s = req.getPathInfo();
        Long id = parseId(s);
        Vehicle vehicle;
        if (id != null) {
            vehicle = vehicleDao.findById(id).orElseThrow(() -> new IllegalStateException("No vehicle with id "+id));
            req.setAttribute("brand",vehicle.getBrand());
            req.setAttribute("model",vehicle.getModel());
            req.setAttribute("price_per_day",formatPrice(vehicle.getPrice_per_day()));
        }
        // przekazuje sterowanie do strony jsp zwracającej formularz z samochodem
        req.getRequestDispatcher("/WEB-INF/views/vehicle/vehicle_form.jsp").forward(req, res);
    }

    private void handleVehicleEditPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String s = req.getPathInfo();
        Long id = parseId(s);

        Map<String,String> fieldToError = new HashMap<>();
        Vehicle vehicle = parseVehicle(req.getParameterMap(),fieldToError);

        if (!fieldToError.isEmpty()) {
            // ustawia błędy jako atrybut do wyrenderowania na stronie z formularzem
            req.setAttribute("errors",fieldToError);
            // ustawia wartości przekazane z formularza metodą POST w atrybutach do wyrenderowania na stronie z formularzem
            req.setAttribute("brand",req.getParameter("brand"));
            req.setAttribute("model",req.getParameter("model"));
            req.setAttribute("price_per_day",req.getParameter("price_per_day"));

            // przekazuje sterowanie do widoku jsp w celu wyrenderowania formularza z informacją o błędach
            req.getRequestDispatcher("/WEB-INF/views/vehicle/vehicle_form.jsp").forward(req, res);
            return;
        }

        if (id != null) vehicle.setId(id);
        vehicleDao.saveOrUpdate(vehicle);

        // po udanej konwersji/walidacji i zapisie obiektu użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą aut
        res.sendRedirect(req.getContextPath() + "/vehicle/list");
    }

    private void handleVehicleRemove(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String s = req.getPathInfo();
        Long id = parseId(s);
        vehicleDao.remove(id);
        // użytkownik jest przekierowywany (przez HTTP Redirect) na stronę z listą pojazdów
        res.sendRedirect(req.getContextPath() + "/vehicle/list");
    }

    private void handleVehicleReserve(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        boolean reserved = Boolean.parseBoolean(req.getParameter("reserved"));

        vehicleDao.reserveVehicle(id);

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", reserved);

        res.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }

    private Vehicle parseVehicle(Map<String,String[]> paramToValue, Map<String,String> fieldToError) {
        String brand = paramToValue.get("brand")[0];
        String model = paramToValue.get("model")[0];
        String price = paramToValue.get("price_per_day")[0];
        BigDecimal bdPrice = null;

        if (brand == null || brand.trim().isEmpty()) {
            fieldToError.put("brand","Pole marka nie może być puste");
        }

        if (model == null || model.trim().isEmpty()) {
            fieldToError.put("model","Pole model nie może być puste");
        }

        if (price == null || price.trim().isEmpty()) {
            fieldToError.put("price_per_day","Pole cena nie może być puste");
        } else {
            try {
                bdPrice = parsePrice(price);
            } catch (Throwable e) {
                fieldToError.put("price_per_day","Cena musi być poprawną liczbą");
            }
        }

        return fieldToError.isEmpty() ?  new Vehicle(brand,model,"2001", bdPrice) : null;
    }


    private BigDecimal parsePrice(String s) throws ParseException {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        Locale locale = new Locale("pl", "PL");
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        format.setParseBigDecimal(true);
        return ((BigDecimal)format.parse(s)).setScale(2, RoundingMode.FLOOR);
    }

    private String formatPrice(BigDecimal price)  {
        if (price == null) return "";
        Locale locale = new Locale("pl", "PL");
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        return format.format(price);
    }

    private Long parseId(String s) {
        if (s == null || !s.startsWith("/"))
            return null;
        return Long.parseLong(s.substring(1));
    }

}
