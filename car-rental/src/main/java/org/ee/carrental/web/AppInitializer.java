package org.ee.carrental.web;

import jakarta.servlet.ServletContextEvent;
import jakarta.servlet.ServletContextListener;
import jakarta.servlet.annotation.WebListener;
import org.ee.carrental.web.service.UserService;

import java.util.logging.Logger;

@WebListener
public class AppInitializer implements ServletContextListener {

    private static final Logger logger = Logger.getLogger(AppInitializer.class.getName());

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        // Kod do inicjalizacji
        createAdminUserIfNeeded();
    }

    private void createAdminUserIfNeeded() {
        logger.info("tworzenie admina");
        UserService userService = new UserService();
        try {
            userService.registerUser("admin", "admin", "ROLE_ADMIN");
        } catch (RuntimeException e) {
            logger.info("admin juz istnieje");
        }

    }
}
