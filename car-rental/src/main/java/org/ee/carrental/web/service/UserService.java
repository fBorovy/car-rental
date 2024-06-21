package org.ee.carrental.web.service;

import jakarta.servlet.http.HttpServletRequest;
import org.ee.carrental.web.dao.UserDaoImpl;
import org.ee.carrental.web.model.User;

public interface UserService {

    void setUserDao(UserDaoImpl userDao);
    User registerUser(String login, String password, String userGroupName);
    User loginUser(String login, String password);
    void logoutUser(HttpServletRequest req);
    User findByUsername(String name);
}