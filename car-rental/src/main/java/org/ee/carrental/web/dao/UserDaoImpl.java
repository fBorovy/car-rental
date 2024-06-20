package org.ee.carrental.web.dao;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.NoResultException;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.ee.carrental.web.model.User;
import org.ee.carrental.web.model.UserGroup;
import org.ee.carrental.web.service.UserService;

import java.util.logging.Logger;

@Stateless
public class UserDaoImpl implements UserDao {

    private static final Logger logger = Logger.getLogger(UserService.class.getName());

    @PersistenceContext(unitName = "pu")
    private EntityManager entityManager;

    // setter for testing
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Override
    public User findByUsername(String username) {
        logger.info("DAO PRZED QUERY _________________________");
        TypedQuery<User> query = entityManager.createQuery(
                "SELECT u FROM User u LEFT JOIN FETCH u.userGroups WHERE u.login = :username", User.class);
        query.setParameter("username", username);
        logger.info("DAO PO QUERY _________________________");

        try {
            logger.info("DAO TRY RETURN _________________________");
            return query.getSingleResult();
        } catch (NoResultException ex) {
            logger.info("DAO  NO RESULT EXCEPTION _________________________");
            return null;
        }
    }

    @Override
    public User save(User user, UserGroup userGroup) {
        entityManager.persist(user);
        entityManager.persist(userGroup);
        return user;
    }
}