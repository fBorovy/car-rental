import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.security.enterprise.identitystore.Pbkdf2PasswordHash;
import org.ee.carrental.web.service.UserService;
import org.ee.carrental.web.dao.UserDaoImpl;
import org.ee.carrental.web.model.User;
import org.junit.jupiter.api.*;

import java.util.HashMap;
import java.util.Map;

public class UserTest {

    static private EntityManager entityManager;
    static private UserDaoImpl userDao;
    static private UserService userService;
    //static private PasswordHasher passwordHasher;
    //static private Pbkdf2PasswordHash passwordHash;
    User sampleUser = new User("Janek", "Panek");

    @BeforeAll
    static public void setup() {
        //Pbkdf2PasswordHash passwordHash = new Pbkdf2PasswordHashImpl();
//        Map<String, String> parameters = new HashMap<>();
//        parameters.put("Pbkdf2PasswordHash.Algorithm", "PBKDF2WithHmacSHA512");
//        parameters.put("Pbkdf2PasswordHash.Iterations", "3072");
//        parameters.put("Pbkdf2PasswordHash.SaltSizeBytes", "64");
        //passwordHash.initialize(parameters);
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_test");
        entityManager = emf.createEntityManager();
        userDao = new UserDaoImpl();
        userDao.setEntityManager(entityManager);
        userService = new UserService();
        userService.setUserDao(userDao);
        //passwordHasher = new PasswordHasher(passwordHash);
        //userService.setPasswordHasher(passwordHasher);
    }

    @BeforeEach
    public void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    @AfterEach
    public void commitTransaction() {
        entityManager.getTransaction().commit();
        entityManager.clear();
    }

    @AfterAll
    static public void tearDown() {
        if (entityManager != null) {
            entityManager.close();
        }
    }

    @Test
    public void testCreateUser() {
        User created = userService.registerUser("Janek", "Panek");
        Assertions.assertNotNull(created.getId());
        Assertions.assertEquals("Janek", created.getLogin());
    }

    @Test
    void testLoginUser() {
        User created = userService.registerUser("Wiktor", "Wektor");
        User logged = userService.loginUser("Wiktor", "Wektor");
        Assertions.assertEquals("Wiktor", logged.getLogin());
    }
}
