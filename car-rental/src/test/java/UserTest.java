import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.ee.carrental.web.service.UserServiceImpl;
import org.ee.carrental.web.dao.UserDaoImpl;
import org.ee.carrental.web.model.User;
import org.junit.jupiter.api.*;

public class UserTest {

    static private EntityManager entityManager;
    static private UserDaoImpl userDao;
    static private UserServiceImpl userService;
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
        userService = new UserServiceImpl();
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
        User created = userService.registerUser("Janek", "Panek", "ROLE_USER");
        Assertions.assertNotNull(created.getId());
        Assertions.assertEquals("Janek", created.getLogin());
    }

    @Test
    void testLoginUser() {
        User created = userService.registerUser("Wiktor", "Wektor", "ROLE_USER");
        User logged = userService.loginUser("Wiktor", "Wektor");
        Assertions.assertEquals("Wiktor", logged.getLogin());
    }

    @Test
    void testFindByUsername() {
        User created = userService.registerUser("Szczur", "Zsilki", "ROLE_USER");
        User user = userService.findByUsername("Szczur");
        Assertions.assertEquals("Zsilki", user.getPassword());
    }
}
