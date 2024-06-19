import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import org.ee.carrental.web.dao.VehicleDaoImpl;
import org.ee.carrental.web.model.Vehicle;
import org.junit.jupiter.api.*;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class VehicleTest {

    static private EntityManager entityManager;
    static private VehicleDaoImpl vehicleDao;
    final private Vehicle sampleVehicle = new Vehicle(
            "Peugeot", "206", "2003", BigDecimal.valueOf(60)
    );

    @BeforeAll
    static public void setup() {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("pu_test");
        entityManager = emf.createEntityManager();
        vehicleDao = new VehicleDaoImpl();
        vehicleDao.setEntityManager(entityManager);
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
    public void testCreateVehicle() {
        Vehicle created = null;
        try {
            created = vehicleDao.saveOrUpdate(sampleVehicle);
        } catch (Exception e) {

        }
        assert created.getId() != null;
    }

    @Test
    public void testFindVehicleById() {
        Vehicle created = vehicleDao.saveOrUpdate(sampleVehicle);
        Optional<Vehicle> found = vehicleDao.findById(created.getId());
        assertTrue(found.isPresent());
        found.ifPresent(vehicle -> {
            assert vehicle.getBrand().equals("Peugeot");
        });
    }

    @Test
    public void testFindVehicleByWrongId(){
        Optional<Vehicle> opt = vehicleDao.findById(0L);
        assertFalse(opt.isPresent());
    }

    @Test
    public void testEditVehicle() {
        sampleVehicle.setId(101010L);
        Vehicle created = vehicleDao.saveOrUpdate(sampleVehicle);
        assert created.getPrice_per_day().equals(BigDecimal.valueOf(60));
        created.setPrice_per_day(BigDecimal.valueOf(80));
        Vehicle newCreated = vehicleDao.saveOrUpdate(created);
        assert newCreated.getPrice_per_day().equals(BigDecimal.valueOf(80)) && newCreated.getId().equals(101010L);
    }

    @Test
    public void testRemoveVehicle() {
        Vehicle created = vehicleDao.saveOrUpdate(sampleVehicle);
        Long id = created.getId();
        vehicleDao.remove(id);
        Optional<Vehicle> opt = vehicleDao.findById(id);
        assertFalse(opt.isPresent());
    }

}
