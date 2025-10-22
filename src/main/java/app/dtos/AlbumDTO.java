package app.dtos;

import app.entities.Album;
import app.entities.Artist;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class AlbumDTO {

    private Integer id;
    private String albumName;
    private LocalDate releaseDate;
    private String totalDuration;
    private Integer artistId;
    private String artistName;

    public AlbumDTO(Album album) {
        this.id = album.getId();
        this.albumName = album.getAlbumName();
        this.releaseDate = album.getReleaseDate();
        this.totalDuration = album.getTotalDuration();
        if (album.getArtist() != null) {
            this.artistId = album.getArtist().getId();
            this.artistName = album.getArtist().getArtistName();
        }
    }

    public Album toEntity() {
        Album album = new Album();
        album.setId(this.id != null ? this.id : 0);
        album.setAlbumName(this.albumName);
        album.setReleaseDate(this.releaseDate);
        album.setTotalDuration(this.totalDuration);

        if (this.artistId != null) {
            Artist artist = new Artist();
            artist.setId(this.artistId);
            artist.setArtistName(this.artistName);
            album.setArtist(artist);
        }

        return album;
    }

    public static List<AlbumDTO> toDTOList(List<Album> albums) {
        return albums.stream().map(AlbumDTO::new).toList();
    }
}
