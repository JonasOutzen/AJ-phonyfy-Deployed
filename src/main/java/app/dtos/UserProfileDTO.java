package app.dtos;

import app.entities.Playlist;
import app.entities.UserProfile;
import app.security.entities.User;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class UserProfileDTO {

    private String username;
    private Set<Integer> playlistIds;

    public UserProfileDTO(UserProfile userProfile) {
        this.username = userProfile.getUsername();
        this.playlistIds = userProfile.getPlaylists().stream()
                .map(Playlist::getId)
                .collect(Collectors.toSet());
    }

    public UserProfile toEntity() {
        UserProfile userProfile = new UserProfile();
        userProfile.setUsername(this.username);

        if (this.playlistIds != null && !this.playlistIds.isEmpty()) {
            Set<Playlist> playlists = this.playlistIds.stream()
                    .map(id -> {
                        Playlist p = new Playlist();
                        p.setId(id);
                        return p;
                    })
                    .collect(Collectors.toSet());
            userProfile.setPlaylists(playlists);
        }

        // Link back to User (optional, only if needed)
        User user = new User();
        user.setUsername(this.username);
        userProfile.setAccount(user);

        return userProfile;
    }

    public static List<UserProfileDTO> toDTOList(List<UserProfile> profiles) {
        return profiles.stream().map(UserProfileDTO::new).toList();
    }
}
