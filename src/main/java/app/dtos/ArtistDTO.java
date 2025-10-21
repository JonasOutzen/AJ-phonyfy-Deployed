package app.dtos;

import app.entities.Artist;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class ArtistDTO {

    private Integer id;
    private String artistName;
    private String type;

    public ArtistDTO(Artist artist) {
        this.id = artist.getId();
        this.artistName = artist.getArtistName();
        this.type = artist.getType();
    }

    public Artist toEntity() {
        Artist artist = new Artist();
        artist.setId(this.id != null ? this.id : 0);
        artist.setArtistName(this.artistName);
        artist.setType(this.type);
        return artist;
    }

    public static List<ArtistDTO> toDTOList(List<Artist> artists) {
        return artists.stream().map(ArtistDTO::new).toList();
    }
}
