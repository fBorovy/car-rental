package org.ee.carrental.web.dao;

import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.UserGroup;

public interface UserDao {

    User save(User user, UserGroup userGroup);
    User findByUsername(String username);
    User findById(Long id);
}
