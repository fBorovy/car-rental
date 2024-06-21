package org.ee.carrental.web;

import jakarta.ejb.EJB;
import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.ee.carrental.web.service.UserService;

import java.util.logging.Logger;

@WebListener
public class AppInitializer implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(AppInitializer.class.getName());
    @EJB
    UserService userService;

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        createAdminUserIfNeeded();
    }

    private void createAdminUserIfNeeded() {
        try {
            userService.registerUser("admin", "admin", "ROLE_ADMIN");
        } catch (Exception e) {
            logger.info(e.getMessage());
        }

    }
}
