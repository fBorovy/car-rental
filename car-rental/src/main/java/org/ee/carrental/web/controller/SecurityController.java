package org.ee.carrental.web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ee.carrental.web.model.User;
import org.ee.carrental.web.service.UserService;

import java.io.IOException;
import java.util.logging.Logger;

@WebServlet(name = "LoginController", urlPatterns = {"/security", "/security/login", "/security/logout", "/security/register"})
public class SecurityController extends HttpServlet {

    private static final Logger logger = Logger.getLogger(SecurityController.class.getName());

    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        logger.info("Wywolano metode doPost()");

        String path = req.getServletPath();
        switch (path) {
            case "/security/login":
                handleLoginPost(req, res);
                break;
            case "/security/register":
                handleRegisterPost(req, res);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        logger.info("Wywolano metode doGet()");

        String path = req.getServletPath();
        switch (path) {
            case "/security/login":
                handleLoginGet(req, res);
                break;
            case "/security/logout":
                handleLogoutGet(req, res);
                break;
            case "/security/register":
                handleRegisterGet(req, res);
                break;
        }
    }

    private void handleRegisterPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        logger.info("Wywolano metode handleRegisterPost()");

        String login = req.getParameter("login");
        String password = req.getParameter("password");
        String re_password = req.getParameter("re_password");

        if (!password.equals(re_password)) {
            req.setAttribute("error", "Hasła nie mogą się różnić");
            logger.info("Wyswietlnie strony z bledem po rejestracji");
            req.getRequestDispatcher("/WEB-INF/views/security/register.jsp").forward(req, res); // przekierowanie z powrotem na stronę logowania z komunikatem o błędzie
        }

        try {
            User user = userService.registerUser(login, password, "ROLE_USER");
            req.getSession().setAttribute("user", user);
            res.sendRedirect(req.getContextPath() + "/vehicle/list"); // przekierowanie na stronę główną po rejestracji
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
            logger.info("Wyswietlnie strony z bledem po rejestracji");
            req.getRequestDispatcher("/WEB-INF/views/security/register.jsp").forward(req, res); // przekierowanie z powrotem na stronę logowania z komunikatem o błędzie
        }
    }

    private void handleRegisterGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        req.getRequestDispatcher("/WEB-INF/views/security/register.jsp").forward(req, res);
    }

    protected void handleLoginGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/security/login.jsp").forward(req, res);
    }

    protected void handleLoginPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        logger.info("Wywolano metode handleLoginPost()");

        String login = req.getParameter("login");
        String password = req.getParameter("password");

        try {
            User user = userService.loginUser(login, password);
            req.getSession().setAttribute("user", user);
            res.sendRedirect(req.getContextPath() + "/vehicle/list"); // przekierowanie na stronę główną po zalogowaniu
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
            logger.info("Wyswietlnie strony z bledem");
            req.getRequestDispatcher("/WEB-INF/views/security/login.jsp").forward(req, res); // przekierowanie z powrotem na stronę logowania z komunikatem o błędzie
        }
    }

    protected void handleLogoutGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        userService.logoutUser(req);
        res.sendRedirect(req.getContextPath() + "security/login.jsp");
    }
}
