package app.daos;

import app.dtos.SampleDTO;
import app.entities.Sample;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;

public class SampleDAO {

    private static SampleDAO instance;
    private static EntityManagerFactory emf;

    private SampleDAO() {}

    public static SampleDAO getInstance(EntityManagerFactory emf) {
        if (instance == null) {
            instance = new SampleDAO();
            SampleDAO.emf = emf;
        }
        return instance;
    }

    public List<SampleDTO> getAllSamples() {
        try (var em = emf.createEntityManager()) {
            TypedQuery<Sample> query = em.createQuery("SELECT s FROM Sample s", Sample.class);
            return SampleDTO.toDTOList(query.getResultList());
        }
    }

    public SampleDTO create(SampleDTO dto) {
        var entity = new Sample(dto);
        try (var em = emf.createEntityManager()) {
            var tx = em.getTransaction();
            tx.begin();
            em.persist(entity);
            tx.commit();
            return new SampleDTO(entity);
        }
    }

    public List<SampleDTO> createFromList(SampleDTO[] dtos) {
        List<SampleDTO> createdList = new ArrayList<>();
        for (SampleDTO dto : dtos) {
            createdList.add(create(dto));
        }
        return createdList;
    }

    public SampleDTO getById(int id) {
        try (var em = emf.createEntityManager()) {
            var entity = em.find(Sample.class, id);
            return (entity != null) ? new SampleDTO(entity) : null;
        }
    }

    // Note: returns null if entity not found
    public SampleDTO update(int id, SampleDTO dto) {
        try (var em = emf.createEntityManager()) {
            Sample entity = em.find(Sample.class, id);
            if (entity != null) {
                var tx = em.getTransaction();
                tx.begin();
                entity.setUserName(dto.getUserName());
                entity.setSampleNumber(dto.getSampleNumber());
                em.merge(entity);
                tx.commit();
                return new SampleDTO(entity);
            }
            return null;
        }
    }

    public void delete(int id) {
        try (var em = emf.createEntityManager()) {
            var tx = em.getTransaction();
            tx.begin();
            var entity = em.find(Sample.class, id);
            if (entity != null) {
                em.remove(entity);
            }
            tx.commit();
        }
    }

    public void deleteAll() {
        try (var em = emf.createEntityManager()) {
            var tx = em.getTransaction();
            tx.begin();
            em.createQuery("DELETE FROM Sample").executeUpdate();
            tx.commit();
        }
    }
}
