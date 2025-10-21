package app.entities;

import app.security.entities.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "user_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserProfile {
    @Id
    @Column(name = "username", length = 25)
    private String username;

    // Relation to security user, shares key with @MapsId
    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @MapsId
    @JoinColumn(name = "username", referencedColumnName = "username",
            foreignKey = @ForeignKey(name = "fk_profile_user"))
    private User account;

    @OneToMany(mappedBy = "owner", fetch = FetchType.LAZY,
            cascade = CascadeType.ALL, orphanRemoval = true)
    @Builder.Default
    private Set<Playlist> playlists = new HashSet<>();

    // Helper method
    public void addPlaylist(Playlist p) {
        playlists.add(p);
        p.setOwner(this);
    }
}
