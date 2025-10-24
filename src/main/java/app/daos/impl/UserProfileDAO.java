package app.daos.impl;

import app.daos.IDAO;
import app.dtos.UserProfileDTO;
import app.dtos.PlaylistDTO;
import app.entities.UserProfile;
import app.entities.Playlist;
import app.security.entities.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class UserProfileDAO implements IDAO<UserProfileDTO, String> {

    private static UserProfileDAO instance;
    private static EntityManagerFactory emf;

    public static UserProfileDAO getInstance(EntityManagerFactory _emf) {
        if (instance == null) {
            emf = _emf;
            instance = new UserProfileDAO();
        }
        return instance;
    }

    @Override
    public UserProfileDTO read(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            UserProfile profile = em.find(UserProfile.class, username);
            return profile == null ? null : new UserProfileDTO(profile);
        }
    }

    @Override
    public List<UserProfileDTO> readAll() {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<UserProfileDTO> q = em.createQuery(
                    "SELECT new app.dtos.UserProfileDTO(up) FROM UserProfile up",
                    UserProfileDTO.class
            );
            return q.getResultList();
        }
    }

    @Override
    public UserProfileDTO create(UserProfileDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            // Build entity from DTO
            UserProfile profile = dto.toEntity();

            // Ensure the linked security User is a managed reference (MapsId)
            if (dto.getUsername() != null) {
                User userRef = em.getReference(User.class, dto.getUsername());
                profile.setAccount(userRef);
                profile.setUsername(dto.getUsername());
            }

            // Attach existing playlists by id (if provided)
            if (dto.getPlaylistIds() != null && !dto.getPlaylistIds().isEmpty()) {
                Set<Playlist> pls = dto.getPlaylistIds().stream()
                        .map(id -> em.getReference(Playlist.class, id))
                        .collect(Collectors.toSet());
                profile.setPlaylists(pls);
                pls.forEach(p -> p.setOwner(profile));
            }

            em.persist(profile);
            em.getTransaction().commit();
            return new UserProfileDTO(profile);
        }
    }

    @Override
    public UserProfileDTO update(String username, UserProfileDTO dto) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();

            UserProfile profile = em.find(UserProfile.class, username);
            if (profile == null) {
                em.getTransaction().commit();
                return null;
            }

            // Ensure the account link stays consistent with MapsId
            if (username != null) {
                User userRef = em.getReference(User.class, username);
                profile.setAccount(userRef);
                profile.setUsername(username);
            }

            // If playlist IDs are provided, replace the set accordingly (optional behavior)
            if (dto.getPlaylistIds() != null) {
                // Clear current
                profile.getPlaylists().forEach(p -> p.setOwner(null));
                profile.getPlaylists().clear();

                // Attach new references
                Set<Playlist> pls = dto.getPlaylistIds().stream()
                        .map(id -> em.getReference(Playlist.class, id))
                        .collect(Collectors.toSet());
                profile.setPlaylists(pls);
                pls.forEach(p -> p.setOwner(profile));
            }

            em.getTransaction().commit();
            return new UserProfileDTO(profile);
        }
    }

    @Override
    public void delete(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            em.getTransaction().begin();
            UserProfile profile = em.find(UserProfile.class, username);
            if (profile != null) {
                profile.getPlaylists().forEach(p -> p.setOwner(null));
                profile.getPlaylists().clear();

                em.remove(profile);
            }
            em.getTransaction().commit();
        }
    }

    @Override
    public boolean validatePrimaryKey(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            return em.find(UserProfile.class, username) != null;
        }
    }

    // Convenience queries

    //  Get playlist IDs owned by this user
    public Set<Integer> getPlaylistIdsByUsername(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            UserProfile profile = em.find(UserProfile.class, username);
            if (profile == null) return Set.of();
            return profile.getPlaylists().stream()
                    .map(Playlist::getId)
                    .collect(Collectors.toSet());
        }
    }

    // Get playlists as DTOs
    public List<PlaylistDTO> getPlaylistsByUsername(String username) {
        try (EntityManager em = emf.createEntityManager()) {
            TypedQuery<Playlist> q = em.createQuery(
                    "SELECT p FROM Playlist p WHERE p.owner.username = :u",
                    Playlist.class
            );
            q.setParameter("u", username);
            return q.getResultList().stream()
                    .map(PlaylistDTO::new)
                    .toList();
        }
    }
}
