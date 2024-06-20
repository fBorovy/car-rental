package org.ee.carrental.web.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.servlet.http.HttpServletRequest;
import org.ee.carrental.web.dao.UserDao;
import org.ee.carrental.web.dao.UserDaoImpl;
import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.UserGroup;

import java.util.logging.Logger;

@Stateless
public class UserService {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @EJB
    private UserDao userDao;

//    @EJB
//    private PasswordHasher passwordHasher;

    //for testing
    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }
//    public void setPasswordHasher(PasswordHasher passwordHasher) {
//        this.passwordHasher = passwordHasher;
//    }


    public User registerUser(String login, String password, String userGroupName) throws RuntimeException {
        //if (login.isEmpty())

        logger.info("wywolano USERSERVICE REGISTER USER _____________________________________");
        User foundUser = userDao.findByUsername(login);
        logger.info("PO USER DAO _____________________________________");
        if (foundUser != null) {
            logger.info("USER FOUND _________");
            throw new RuntimeException("Username already in use");
        }
        logger.info("USER NOT FOUND _________  CREATION PROCEED");
        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);//(passwordHasher.hash(password));
        UserGroup userGroup = new UserGroup(newUser, userGroupName);
        return userDao.save(newUser, userGroup);
    }


    public User loginUser(String login, String password) throws RuntimeException {
        logger.info("wywolano userservice loginUser");
        User foundUser = userDao.findByUsername(login);
        if (foundUser == null) {
            logger.info("wywolano userservice loginUser- nie znaleziono usera");
            throw new RuntimeException("Invalid username");
        }
        //if (!passwordHasher.verify(password, foundUser.getPassword())) {
        if (!password.equals(foundUser.getPassword())) {
            throw new RuntimeException("Invalid password");
        }
        return foundUser;
    }

    public void logoutUser(HttpServletRequest req) {
        req.getSession().invalidate();
    }

    public User findByUsername(String name) {
        return userDao.findByUsername(name);
    }
}
