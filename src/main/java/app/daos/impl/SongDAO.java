package app.daos.impl;


import app.daos.IDAO;
import app.dtos.SongDTO;
import app.entities.Album;
import app.entities.Artist;
import app.entities.Song;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SongDAO implements IDAO<SongDTO, Integer> {

    private static SongDAO instance;
    private static EntityManagerFactory emf;


    public static SongDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new SongDAO();
        }
        return instance;
    }

    @Override
    public SongDTO read(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            Song song = em.find(Song.class, integer);
            return new SongDTO(song);
        }
    }

    @Override
    public List<SongDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<SongDTO> query = em.createQuery("SELECT new app.dtos.SongDTO(s) FROM Song s", SongDTO.class);
            return query.getResultList();
        }
    }

    @Override
    public SongDTO create(SongDTO songDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            if (songDTO.getMainArtistId() != null) {
                throw new IllegalArgumentException("Main Artist ID is required");
            }

            if (songDTO.getAlbumId() != null) {
                throw new IllegalArgumentException("Album ID is required");
            }

            Song s = songDTO.toEntity();
            s.setSongId(null);
            s.setMainArtist(em.getReference(Artist.class, songDTO.getMainArtistId()));
            s.setAlbum(em.getReference(Album.class, songDTO.getAlbumId()));

            em.persist(s);
            em.getTransaction().commit();
            return new SongDTO(s);
        }
    }

    @Override
    public SongDTO update(Integer integer, SongDTO songDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Song s = em.find(Song.class, integer);
            if (s == null) throw new IllegalArgumentException("Song not found: " + integer);

            s.setSongName(songDTO.getSongName());
            s.setGenre(songDTO.getGenre());
            s.setFeaturedArtist(songDTO.getFeaturedArtist());
            if (songDTO.getDuration() != null) {
                s.setDuration(songDTO.getDuration());
            }

            // Relations, if they are provided
            if (songDTO.getMainArtistId() != null) {
                s.setMainArtist(em.getReference(Artist.class, songDTO.getMainArtistId()));
            }
            if (songDTO.getAlbumId() != null) {
                s.setAlbum(em.getReference(Album.class, songDTO.getAlbumId()));
            }

            Song mergedSong = em.merge(s);
            em.getTransaction().commit();

            return new SongDTO(mergedSong);
        }
    }

    @Override
    public void delete(Integer integer) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Song s = em.find(Song.class, integer);
            if (s != null) {
                em.remove(s);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer integer) {
        try  (EntityManager em = emf.createEntityManager()) {
            Song s = em.find(Song.class, integer);
            return s != null;
        }
    }


}
