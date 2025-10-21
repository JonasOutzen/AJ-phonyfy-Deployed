package app.dtos;

import app.entities.Album;
import app.entities.Artist;
import app.entities.Song;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class SongDTO {

    private Integer songId;
    private String songName;
    private String genre;
    private String featuredArtist;
    private String duration;
    private Integer mainArtistId;
    private String mainArtistName;
    private Integer albumId;
    private String albumName;

    public SongDTO(Song song) {
        this.songId = song.getSongId();
        this.songName = song.getSongName();
        this.genre = song.getGenre();
        this.featuredArtist = song.getFeaturedArtist();
        this.duration = song.getDuration();

        if (song.getMainArtist() != null) {
            this.mainArtistId = song.getMainArtist().getId();
            this.mainArtistName = song.getMainArtist().getArtistName();
        }

        if (song.getAlbum() != null) {
            this.albumId = song.getAlbum().getId();
            this.albumName = song.getAlbum().getAlbumName();
        }
    }

    public Song toEntity() {
        Song song = new Song();
        song.setSongId(this.songId);
        song.setSongName(this.songName);
        song.setGenre(this.genre);
        song.setFeaturedArtist(this.featuredArtist);
        song.setDuration(this.duration);

        if (this.mainArtistId != null) {
            Artist artist = new Artist();
            artist.setId(this.mainArtistId);
            artist.setArtistName(this.mainArtistName);
            song.setMainArtist(artist);
        }

        if (this.albumId != null) {
            Album album = new Album();
            album.setId(this.albumId);
            album.setAlbumName(this.albumName);
            song.setAlbum(album);
        }

        return song;
    }

    public static List<SongDTO> toDTOList(List<Song> songs) {
        return songs.stream().map(SongDTO::new).toList();
    }
}
