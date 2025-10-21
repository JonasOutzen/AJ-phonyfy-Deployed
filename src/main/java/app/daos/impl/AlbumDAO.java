package app.daos.impl;

import app.daos.IDAO;
import app.dtos.AlbumDTO;
import app.entities.Album;
import app.entities.Artist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;

public class AlbumDAO implements IDAO<AlbumDTO, Integer> {

    private static AlbumDAO instance;
    private static EntityManagerFactory emf;

    public static AlbumDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new AlbumDAO();
        }
        return instance;
    }

    @Override
    public AlbumDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Album album = em.find(Album.class, id);
            return album == null ? null : new AlbumDTO(album);
        }
    }

    @Override
    public List<AlbumDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<AlbumDTO> query = em.createQuery(
                    "SELECT new app.dtos.AlbumDTO(a) FROM Album a",
                    AlbumDTO.class
            );
            return query.getResultList();
        }
    }

    @Override
    public AlbumDTO create(AlbumDTO albumDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Album album = albumDTO.toEntity();

            // ensure artist relation is a managed reference
            if (albumDTO.getArtistId() != null) {
                Artist artistRef = em.getReference(Artist.class, albumDTO.getArtistId());
                album.setArtist(artistRef);
            }

            em.persist(album);
            em.getTransaction().commit();
            return new AlbumDTO(album);
        }
    }

    @Override
    public AlbumDTO update(Integer id, AlbumDTO albumDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Album album = em.find(Album.class, id);
            if (album == null) {
                em.getTransaction().commit(); // nothing to update; keep style consistent
                return null;
            }

            album.setAlbumName(albumDTO.getAlbumName());
            album.setReleaseDate(albumDTO.getReleaseDate());
            album.setTotalDuration(albumDTO.getTotalDuration());

            if (albumDTO.getArtistId() != null) {
                Artist artistRef = em.getReference(Artist.class, albumDTO.getArtistId());
                album.setArtist(artistRef);
            }

            // album is managed; merge not required
            em.getTransaction().commit();
            return new AlbumDTO(album);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Album album = em.find(Album.class, id);
            if (album != null) {
                em.remove(album);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Album album = em.find(Album.class, id);
            return album != null;
        }
    }
}
