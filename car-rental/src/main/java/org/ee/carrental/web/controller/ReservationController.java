package org.ee.carrental.web.controller;

import jakarta.ejb.EJB;
import jakarta.inject.Inject;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jdk.jfr.Name;
import org.ee.carrental.web.dao.*;
import org.ee.carrental.web.model.Reservation;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.Vehicle;
import org.ee.carrental.web.service.GMailer;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Name("ReservationDao")
@WebServlet(name = "ReservationController", urlPatterns = {"/reservation/list", "/reservation/edit/*", "/reservation/form/*", "/reservation/remove/*", "/reservation/cancel/*"})
public class ReservationController extends HttpServlet {

    @EJB
    private ReservationDao reservationDao;

    @Inject
    private VehicleDao vehicleDao;

    @Inject
    private UserDao userDao;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();

        switch (path) {
            case "/reservation/list":
                handleReservationList(req, res);
                break;
            case "/reservation/edit":
                if (isUserAuthorized(req, "ROLE_ADMIN")) {
                    handleReservationEditGet(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                break;
            case "/reservation/remove":
                if (isUserAuthorized(req, "ROLE_ADMIN")) {
                    handleReservationRemove(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                break;
            case "/reservation/form":
                handleReservationForm(req, res);
                break;
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/reservation/form":
                    handleReservationEditPost(req, res);
                break;
            case "/reservation/cancel":
                if (isUserAuthorized(req, "ROLE_USER")) {
                    handleReservationCancel(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                break;
        }
    }

    private void handleReservationList(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        List<Reservation> reservations = reservationDao.findAll();

        // Mapa do przechowywania nazw użytkowników
        Map<Long, String> usernames = new HashMap<>();

        // Pętla przez rezerwacje w celu pobrania nazw użytkowników
        for (Reservation reservation : reservations) {
            User user = userDao.findById(reservation.getReserved_user_id());
            if (user != null) {
                usernames.put(reservation.getId(), user.getLogin());
            } else {
                usernames.put(reservation.getId(), "Unknown");
            }
        }

        req.setAttribute("reservationList", reservations);
        req.setAttribute("usernames", usernames);
        req.getRequestDispatcher("/WEB-INF/views/reservation/reservation_list.jsp").forward(req, res);
    }

    private void handleReservationForm(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String vehicleId = req.getParameter("vehicleId");
        if (vehicleId != null && !vehicleId.isEmpty()) {
            Vehicle vehicle = getVehicleById(Long.parseLong(vehicleId));
            if (vehicle != null) {
                req.setAttribute("brand", vehicle.getBrand());
                req.setAttribute("model", vehicle.getModel());
                req.setAttribute("price", vehicle.getPrice_per_day());
            } else {
                req.setAttribute("error", "Vehicle not found");
            }
        } else {
            req.setAttribute("error", "Invalid vehicle ID");
        }
        req.getRequestDispatcher("/WEB-INF/views/reservation/reservation_form.jsp").forward(req, res);
    }

    private void handleReservationEditGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String s = req.getPathInfo();
        Long id = parseId(s);
        Reservation reservation = getReservation(id);
        if (reservation != null) {
            req.setAttribute("reservation_start", reservation.getReservation_start());
            req.setAttribute("reservation_end", reservation.getReservation_end());
            req.setAttribute("reservation_date", reservation.getReservation_date());
            req.setAttribute("reserved_vehicle_id", reservation.getReserved_vehicle_id());
            req.setAttribute("reserved_user_id", reservation.getReserved_user_id());
            req.setAttribute("payment_status", reservation.getPayment_status());
            req.setAttribute("price", reservation.getPrice());

            VehicleDaoImpl dao = new VehicleDaoImpl();
            Optional<Vehicle> vehicleOptional = dao.findById(reservation.getReserved_vehicle_id());
            if (vehicleOptional.isPresent()) {
                Vehicle vehicle = vehicleOptional.get();
                req.setAttribute("vehicle_brand", vehicle.getBrand());
                req.setAttribute("vehicle_model", vehicle.getModel());
            }
        }
        req.getRequestDispatcher("/WEB-INF/views/reservation/reservation_form.jsp").forward(req, res);
    }

    private void handleReservationEditPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String s = req.getPathInfo();
        Long id = parseId(s);
        Map<String, String> fieldToError = new HashMap<>();
        Reservation reservation = parseReservation(req ,req.getParameterMap(), fieldToError);

        if (!fieldToError.isEmpty()) {
            req.setAttribute("errors", fieldToError);
            setRequestAttributesFromParameters(req);
            req.getRequestDispatcher("/WEB-INF/views/reservation/reservation_form.jsp").forward(req, res);
            return;
        }

        if (id != null) reservation.setId(id);
        Long userId = (Long) req.getSession().getAttribute("id");

        reservation.setReservation_date(new Date()); // Set current date as reservation date
        reservation.setPayment_status(false);
        reservation.setReserved_user_id(userId);

        List<Reservation> reservations = reservationDao.findActiveReservationsByVehicleId(reservation.getReserved_vehicle_id());

        if (!checkDateAvailability(reservation.getReservation_start(), reservation.getReservation_end(), reservations)) {
            fieldToError.put("start_date", "Podana data jest już zajęta, proszę o wybranie innego terminu rezerwacji.");
            req.setAttribute("errors", fieldToError);
            setRequestAttributesFromParameters(req);
            req.getRequestDispatcher("/WEB-INF/views/reservation/reservation_form.jsp").forward(req, res);
            return;
        }

        reservationDao.saveOrUpdate(reservation);

        Vehicle vehicle = getVehicleById(reservation.getReserved_vehicle_id());

        try {
            new GMailer().sendEmail((String) req.getSession().getAttribute("username"),vehicle.getBrand(),vehicle.getModel(),String.valueOf(reservation.getId()), "confirmedReservation");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        res.sendRedirect(req.getContextPath() + "/reservation/list");
    }

    private void setRequestAttributesFromParameters(HttpServletRequest req) {
        req.setAttribute("reservation_start", req.getParameter("start_date"));
        req.setAttribute("reservation_end", req.getParameter("end_date"));
        req.setAttribute("reservation_date", req.getParameter("reservation_date"));
        req.setAttribute("reserved_vehicle_id", req.getParameter("reserved_vehicle_id"));
        req.setAttribute("reserved_user_id", req.getParameter("reserved_user_id"));
        req.setAttribute("payment_status", req.getParameter("payment_status"));
        req.setAttribute("price", req.getParameter("price"));
    }

    private boolean checkDateAvailability(Date startDate, Date endDate, List<Reservation> reservations) {
        for (Reservation reservation : reservations) {
            if (reservation.getReservation_status() &&
                    !startDate.after(reservation.getReservation_end()) &&
                    !endDate.before(reservation.getReservation_start())) {
                return false;
            }
        }
        return true;
    }

    private void handleReservationRemove(HttpServletRequest req, HttpServletResponse res) throws IOException {
        String s = req.getPathInfo();
        Long id = parseId(s);
        reservationDao.remove(id);
        res.sendRedirect(req.getContextPath() + "/reservation/list");
    }

    private void handleReservationCancel(HttpServletRequest req, HttpServletResponse res) throws IOException {
        Long id = Long.parseLong(req.getParameter("id"));
        reservationDao.cancelReservation(id);

        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        Map<String, Object> response = new HashMap<>();
        response.put("success", true);
        response.put("status", "canceled");

        res.getWriter().write(new ObjectMapper().writeValueAsString(response));
    }

    private Reservation parseReservation(HttpServletRequest req, Map<String, String[]> paramToValue, Map<String, String> fieldToError) {
        // Sprawdzenie obecności kluczy i odpowiednich wartości w mapie paramToValue
        if (paramToValue.containsKey("start_date") && paramToValue.get("start_date") != null &&
                paramToValue.containsKey("end_date") && paramToValue.get("end_date") != null &&
                paramToValue.containsKey("vehicle_id") && paramToValue.get("vehicle_id") != null &&
                paramToValue.containsKey("total_price") && paramToValue.get("total_price") != null) {

            Date reservationStart = parseDate(paramToValue.get("start_date")[0], fieldToError, "start_date");
            Date reservationEnd = parseDate(paramToValue.get("end_date")[0], fieldToError, "end_date");
            long reservedVehicleId = parseLong(stripNonNumeric(paramToValue.get("vehicle_id")[0]), fieldToError, "vehicle_id");
            BigDecimal price = parsePrice(stripNonNumeric(paramToValue.get("total_price")[0]));
            Long userId = (Long) req.getSession().getAttribute("id");

            if (fieldToError.isEmpty()) {
                return new Reservation(reservationStart, reservationEnd, new Date(), reservedVehicleId, userId, false, price, true);
            }
        } else {
            // Dodanie błędu, jeśli któryś z wymaganych parametrów jest nieobecny
            if (!paramToValue.containsKey("start_date") || paramToValue.get("start_date") == null) {
                fieldToError.put("start_date", "Missing reservation start date");
            }
            if (!paramToValue.containsKey("end_date") || paramToValue.get("end_date") == null) {
                fieldToError.put("end_date", "Missing reservation end date");
            }
            if (!paramToValue.containsKey("vehicle_id") || paramToValue.get("vehicle_id") == null) {
                fieldToError.put("vehicle_id", "Missing reserved vehicle ID");
            }
            if (!paramToValue.containsKey("total_price") || paramToValue.get("total_price") == null) {
                fieldToError.put("total_price", "Missing price");
            }
        }
        return null;
    }

    private String stripNonNumeric(String input) {
        return input.replaceAll("[^\\d.]", "");
    }

    private Date parseDate(String dateString, Map<String, String> fieldToError, String fieldName) {
        try {
            return new SimpleDateFormat("yyyy-MM-dd").parse(dateString);
        } catch (ParseException e) {
            fieldToError.put(fieldName, "Invalid date format");
            return null;
        }
    }

    private long parseLong(String s, Map<String, String> fieldToError, String fieldName) {
        try {
            return Long.parseLong(s);
        } catch (NumberFormatException e) {
            fieldToError.put(fieldName, "Invalid number format");
            return 0;
        }
    }

    private int parseInt(String s, Map<String, String> fieldToError, String fieldName) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            fieldToError.put(fieldName, "Invalid number format");
            return 0;
        }
    }

    private BigDecimal parsePrice(String s) {
        if (s == null || s.trim().isEmpty()) {
            return null;
        }
        Locale locale = new Locale("pl", "PL");
        DecimalFormat format = (DecimalFormat) NumberFormat.getNumberInstance(locale);
        format.setParseBigDecimal(true);
        try {
            return ((BigDecimal)format.parse(s)).setScale(2, RoundingMode.FLOOR);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    private Long parseId(String s) {
        if (s == null || !s.startsWith("/"))
            return null;
        return Long.parseLong(s.substring(1));
    }

    private Reservation getReservation(Long id) {
        if (id != null) {
            return reservationDao.findById(id).orElseThrow(() -> new IllegalStateException("No reservation with id " + id));
        }
        return null;
    }

    private Vehicle getVehicleById(Long vehicleId) {
        return vehicleDao.findById(vehicleId).orElse(null);
    }

    private boolean isUserAuthorized(HttpServletRequest req, String role) {
        List<String> userGroups = (List<String>) req.getSession().getAttribute("userGroups");
        return userGroups != null && userGroups.contains(role);
    }
}
