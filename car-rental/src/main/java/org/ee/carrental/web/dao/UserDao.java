package org.ee.carrental.web.dao;

import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.UserGroup;
import org.ee.carrental.web.model.Vehicle;

import java.util.Optional;

public interface UserDao {

    User save(User user, UserGroup userGroup);
    User findByUsername(String username);

}
