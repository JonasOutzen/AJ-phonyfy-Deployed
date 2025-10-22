package app.daos.impl;

import app.daos.IDAO;
import app.dtos.PlaylistDTO;
import app.entities.Playlist;
import app.entities.Song;
import app.entities.UserProfile;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PlaylistDAO implements IDAO<PlaylistDTO, Integer> {

    private static PlaylistDAO instance;
    private static EntityManagerFactory emf;

    public static PlaylistDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new PlaylistDAO();
        }
        return instance;
    }

    @Override
    public PlaylistDTO read(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            Playlist p = em.find(Playlist.class, id);
            return p == null ? null : new PlaylistDTO(p);
        }
    }

    public List<PlaylistDTO> readPlaylistsByOwner(String username) {
        try (var em = emf.createEntityManager()) {
            var q = em.createQuery(
                    "SELECT new app.dtos.PlaylistDTO(p) FROM Playlist p WHERE p.owner.username = :u",
                    PlaylistDTO.class
            );
            q.setParameter("u", username);
            return q.getResultList();
        }
    }

    @Override
    public List<PlaylistDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<PlaylistDTO> q = em.createQuery(
                    "SELECT new app.dtos.PlaylistDTO(p) FROM Playlist p",
                    PlaylistDTO.class
            );
            return q.getResultList();
        }
    }

    @Override
    public PlaylistDTO create(PlaylistDTO playlistDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // We have made it so that owner is required (optional=false)
            if (playlistDTO.getOwnerUsername() == null) {
                throw new IllegalArgumentException("ownerUsername is required");
            }

            Playlist p = playlistDTO.toEntity();
            // We let DB autogenerate the ID of the playlist
            p.setId(0);

            UserProfile ownerRef = em.getReference(UserProfile.class, playlistDTO.getOwnerUsername());
            p.setOwner(ownerRef);

            if (playlistDTO.getSongIds() != null && !playlistDTO.getSongIds().isEmpty()) {
                Set<Song> refs = new HashSet<>();
                for (Integer songId : playlistDTO.getSongIds()) {
                    refs.add(em.getReference(Song.class, songId));
                }
                p.setSongs(refs);
            }

            // Our @PrePersist will recalculate the totaltDuration
            em.persist(p);
            em.getTransaction().commit();
            return new PlaylistDTO(p);
        }
    }

    @Override
    public PlaylistDTO update(Integer id, PlaylistDTO playlistDTO) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            Playlist p = em.find(Playlist.class, id);
            if (p == null) throw new IllegalArgumentException("Playlist not found: " + id);

            if (playlistDTO.getPlayListName() != null) {
                p.setPlayListName(playlistDTO.getPlayListName());
            }

            if (playlistDTO.getOwnerUsername() != null) {
                UserProfile ownerRef = em.getReference(UserProfile.class, playlistDTO.getOwnerUsername());
                p.setOwner(ownerRef);
            }

            if (playlistDTO.getSongIds() != null) {
                Set<Song> newSongs = new HashSet<>();
                for (Integer songId : playlistDTO.getSongIds()) {
                    newSongs.add(em.getReference(Song.class, songId));
                }
                p.setSongs(newSongs);
            }

            Playlist mergedPlaylist = em.merge(p);
            em.getTransaction().commit();
            return new PlaylistDTO(mergedPlaylist);
        }
    }

    @Override
    public void delete(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            Playlist p = em.find(Playlist.class, id);
            if (p != null) em.remove(p);
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(Integer id) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(Playlist.class, id) != null;
        }
    }
}