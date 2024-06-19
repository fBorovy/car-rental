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

@WebServlet(name = "LoginController", urlPatterns = "/login")
public class LoginController extends HttpServlet {


    @EJB
    private UserService userService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/login":
                handleLoginPost(req, res);
                break;
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String path = req.getServletPath();
        switch (path) {
            case "/logout":
                handleLogoutGet(req, res);
                break;
        }
    }


    protected void handleLoginPost(HttpServletRequest req, HttpServletResponse res) throws ServletException, IOException {
        String login = req.getParameter("login");
        String password = req.getParameter("password");

        try {
            User user = userService.loginUser(login, password);
            req.getSession().setAttribute("user", user);
            res.sendRedirect(req.getContextPath() + "/WEB-INF/views/vehicle/vehicle_list.jsp\""); // przekierowanie na stronę główną po zalogowaniu
        } catch (RuntimeException e) {
            req.setAttribute("error", e.getMessage());
            req.getRequestDispatcher("/login.jsp").forward(req, res); // przekierowanie z powrotem na stronę logowania z komunikatem o błędzie
        }
    }

    protected void handleLogoutGet(HttpServletRequest req, HttpServletResponse res) throws IOException {
        userService.logoutUser(req);
        res.sendRedirect(req.getContextPath() + "/login.jsp");
    }


}
