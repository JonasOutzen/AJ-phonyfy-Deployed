package app.daos.impl;

import app.daos.IDAO;
import app.dtos.AlbumDTO;
import app.dtos.ArtistDTO;
import app.entities.Artist;
import app.entities.Album;
import app.entities.Song;
import app.entities.UserProfile;
import app.entities.Playlist;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Set;

public class ArtistDAO implements IDAO<ArtistDTO, Integer> {

    private static ArtistDAO instance;
    private static EntityManagerFactory emf;

    public static ArtistDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new ArtistDAO();
        }
        return instance;
    }

    @Override
    public ArtistDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Artist artist = em.find(Artist.class, integer);
            return new ArtistDTO(artist);
        }
    }

    public List<AlbumDTO> readAlbumsByArtistId(Integer artistId) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<AlbumDTO> query = em.createQuery(
                    "SELECT new app.dtos.AlbumDTO(a) FROM Album a WHERE a.artist.id = :artistId",
                    AlbumDTO.class
            );
            query.setParameter("artistId", artistId);
            return query.getResultList();
        }
    }

    @Override
    public List<ArtistDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<ArtistDTO> query = em.createQuery("SELECT new app.dtos.ArtistDTO(a) FROM Artist a", ArtistDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public ArtistDTO create(ArtistDTO artistDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Artist artist = artistDTO.toEntity();
            em.persist(artist);
            em.getTransaction().commit();
            return new ArtistDTO(artist);
        }
    }

    @Override
    public ArtistDTO update(Integer integer, ArtistDTO artistDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Artist artist = em.find(Artist.class, integer);
            artist.setArtistName(artistDTO.getArtistName());
            artist.setType(artistDTO.getType());
            Artist mergedArtist = em.merge(artist);
            em.getTransaction().commit();
            return mergedArtist != null ? new ArtistDTO(mergedArtist) : null;
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Artist artist = em.find(Artist.class, integer);
            if (artist != null) {
                em.remove(artist);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Artist artist = em.find(Artist.class, integer);
            return artist != null;
        }
    }
}
