package org.ee.carrental.web.dao;

import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.Vehicle;

import java.util.Optional;

public interface UserDao {

    User save(User user);
    User findByUsername(String username);

}
