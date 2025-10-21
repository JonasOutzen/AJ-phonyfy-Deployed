package app.dtos;

import app.entities.Playlist;
import app.entities.Song;
import app.entities.UserProfile;
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
public class PlaylistDTO {

    private Integer id;
    private String playListName;
    private String totalDuration;
    private String ownerUsername;
    private Set<Integer> songIds;

    public PlaylistDTO(Playlist playlist) {
        this.id = playlist.getId();
        this.playListName = playlist.getPlayListName();
        this.totalDuration = playlist.getTotalDuration();
        this.ownerUsername = playlist.getOwner().getUsername();
        this.songIds = playlist.getSongs().stream()
                .map(Song::getSongId)
                .collect(Collectors.toSet());
    }

    public Playlist toEntity() {
        Playlist playlist = new Playlist();
        playlist.setId(this.id != null ? this.id : 0);
        playlist.setPlayListName(this.playListName);
        playlist.setTotalDuration(this.totalDuration);

        if (this.ownerUsername != null) {
            UserProfile owner = new UserProfile();
            owner.setUsername(this.ownerUsername);
            playlist.setOwner(owner);
        }

        if (this.songIds != null && !this.songIds.isEmpty()) {
            Set<Song> songs = this.songIds.stream()
                    .map(id -> {
                        Song s = new Song();
                        s.setSongId(id);
                        return s;
                    })
                    .collect(Collectors.toSet());
            playlist.setSongs(songs);
        }

        return playlist;
    }

    public static List<PlaylistDTO> toDTOList(List<Playlist> playlists) {
        return playlists.stream().map(PlaylistDTO::new).toList();
    }
}
