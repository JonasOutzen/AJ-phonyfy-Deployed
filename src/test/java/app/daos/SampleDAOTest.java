package app.daos;

import app.config.HibernateConfig;

import jakarta.persistence.EntityManagerFactory;
import org.junit.jupiter.api.*;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class SampleDAOTest {

    private static EntityManagerFactory emf;
//    private static SampleDAO dao;

//    @BeforeAll
//    static void setup() {
//        emf = HibernateConfig.getEntityManagerFactoryForTest();
//        dao = SampleDAO.getInstance(emf);
//    }
//
//    @BeforeEach
//    void clearDb() {
//        dao.deleteAll();
//    }
//
//    @Test
//    void testCreateAndGetById() {
//        SampleDTO dto = new SampleDTO(null, "Alice", 42);
//        SampleDTO saved = dao.create(dto);
//
//        assertNotNull(saved.getId());
//        assertEquals("Alice", saved.getUserName());
//
//        SampleDTO fetched = dao.getById(saved.getId());
//        assertEquals(saved.getUserName(), fetched.getUserName());
//    }
//
//    @Test
//    void testGetAllSamples() {
//        dao.create(new SampleDTO(null, "A", 1));
//        dao.create(new SampleDTO(null, "B", 2));
//
//        List<SampleDTO> list = dao.getAllSamples();
//        assertEquals(2, list.size());
//    }
//
//    @Test
//    void testUpdateSample() {
//        SampleDTO saved = dao.create(new SampleDTO(null, "C", 3));
//        SampleDTO updated = dao.update(saved.getId(), new SampleDTO(null, "Charlie", 33));
//
//        assertEquals("Charlie", updated.getUserName());
//        assertEquals(33, updated.getSampleNumber());
//    }
//
//    @Test
//    void testDeleteSample() {
//        SampleDTO saved = dao.create(new SampleDTO(null, "D", 4));
//        dao.delete(saved.getId());
//
//        assertNull(dao.getById(saved.getId()));
//    }
}
