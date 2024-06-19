package org.ee.carrental.web.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.servlet.http.HttpServletRequest;
import org.ee.carrental.web.dao.UserDao;
import org.ee.carrental.web.dao.UserDaoImpl;
import org.ee.carrental.web.model.User;

@Stateless
public class UserService {

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


    public User registerUser(String login, String password) throws RuntimeException {
        User foundUser = userDao.findByUsername(login);
        if (foundUser != null) {
            throw new RuntimeException("Username already in use");
        }
        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);//(passwordHasher.hash(password));
        return userDao.save(newUser);
    }


    public User loginUser(String login, String password) throws RuntimeException {
        User foundUser = userDao.findByUsername(login);
        if (foundUser == null) {
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

}
