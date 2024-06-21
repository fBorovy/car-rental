package org.ee.carrental.web.controller;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.UserGroup;
import org.ee.carrental.web.service.UserService;

import java.io.IOException;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

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
                if (req.getSession().getAttribute("user") == null) {
                    handleLoginPost(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                }
                break;
            case "/security/register":
                if (req.getSession().getAttribute("user") == null) {
                    handleRegisterPost(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                }
                break;
            case "/security/logout":
                if (req.getSession().getAttribute("user") != null) {
                    handleLogoutPost(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        logger.info("Wywolano metode doGet()");

        String path = req.getServletPath();
        switch (path) {
            case "/security/login":
                if (req.getSession().getAttribute("user") == null) {
                    handleLoginGet(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                }
                break;
            case "/security/register":
                if (req.getSession().getAttribute("user") == null) {
                    handleRegisterGet(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                }
                break;
            case "/security/logout":
                if (req.getSession().getAttribute("user") != null) {
                    handleLogoutPost(req, res);
                } else {
                    res.sendError(HttpServletResponse.SC_UNAUTHORIZED);
                }
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
            req.getRequestDispatcher("/WEB-INF/views/security/register.jsp").forward(req, res);
        }

        try {
            User user = userService.registerUser(login, password, "ROLE_USER");
            req.getSession().setAttribute("user", user);
            req.getSession().setAttribute("username", user.getLogin());
            List<String> userGroupNames = user.getUserGroups().stream()
                    .map(UserGroup::getName)
                    .collect(Collectors.toList());
            req.getSession().setAttribute("userGroups", userGroupNames);
            res.sendRedirect(req.getContextPath() + "/vehicle/list");
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/security/register.jsp").forward(req, res);
        }
    }

    private void handleRegisterGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException{
        req.getRequestDispatcher("/WEB-INF/views/security/register.jsp").forward(req, res);
    }

    protected void handleLoginGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        req.getRequestDispatcher("/WEB-INF/views/security/login.jsp").forward(req, res);
    }

    protected void handleLoginPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        try {
            User user = userService.loginUser(login, password);
            req.getSession().setAttribute("user", user);
            req.getSession().setAttribute("username", user.getLogin());
            List<String> userGroupNames = user.getUserGroups().stream()
                    .map(UserGroup::getName)
                    .collect(Collectors.toList());
            req.getSession().setAttribute("userGroups", userGroupNames);
            res.sendRedirect(req.getContextPath() + "/vehicle/list");
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/WEB-INF/views/security/login.jsp").forward(req, res);
        }
    }

    protected void handleLogoutPost(HttpServletRequest req, HttpServletResponse res) throws IOException {
        userService.logoutUser(req);
        res.sendRedirect(req.getContextPath() + "/vehicle/list");
    }
}
