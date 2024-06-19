package org.ee.carrental.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.Vehicle;

import java.util.Optional;

@Stateless
public class UserDaoImpl implements UserDao {

    @PersistenceContext(unitName = "pu")
    private EntityManager entityManager;

    // setter for testing
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findByUsername(String username) {
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u WHERE u.login = :username", User.class);
        query.setParameter("username", username);
        try {
            return query.getSingleResult();
        } catch (NoResultException ex) {
            return null;
        }
    }

    @Override
    public User save(User user) {
        entityManager.persist(user);
        return user;
    }
}