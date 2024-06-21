package org.ee.carrental.web.service;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import jakarta.servlet.http.HttpServletRequest;
import org.ee.carrental.web.dao.UserDao;
import org.ee.carrental.web.dao.UserDaoImpl;
import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.UserGroup;

@Stateless
public class UserServiceImpl implements UserService {

    @EJB
    private UserDao userDao;

    //for testing
    public void setUserDao(UserDaoImpl userDao) {
        this.userDao = userDao;
    }


    public User registerUser(String login, String password, String userGroupName) throws RuntimeException {
        User foundUser = userDao.findByUsername(login);
        if (foundUser != null) {
            throw new RuntimeException("Username already in use");
        }
        User newUser = new User();
        newUser.setLogin(login);
        newUser.setPassword(password);
        UserGroup userGroup = new UserGroup(newUser, userGroupName);
        return userDao.save(newUser, userGroup);
    }


    public User loginUser(String login, String password) throws RuntimeException {
        User foundUser = userDao.findByUsername(login);
        if (foundUser == null) {
            throw new RuntimeException("Invalid username");
        }
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
