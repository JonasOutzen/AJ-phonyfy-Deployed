package app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;
import java.time.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder


public class Album {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "album_id", length = 100, nullable = false)
    private int id;

    @Column(name = "album_name", length = 100, nullable = false)
    private String albumName;

    @Column(name = "release_date", length = 100, nullable = false)
    private LocalDate releaseDate;

    @Column(name = "total_duration", nullable = false)
    private String totalDuration = "0:00";


    // Relations
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "artist_id", nullable = false,
            foreignKey = @ForeignKey(name = "fk_album_artist"))
    @ToString.Exclude
    private Artist artist;

    @OneToMany(mappedBy = "album", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = false)
    @Builder.Default
    @ToString.Exclude
    private Set<Song> songs = new HashSet<>();


    public void updateTotalDuration() {
        int totalSeconds = songs.stream()
                .mapToInt(song -> {
                    // If your Song stores "duration" as "mm:ss"
                    // replace with song.getDurationSeconds() if you store seconds instead
                    String d = song.getDuration(); // e.g. "3:45"
                    String[] parts = d.split(":");
                    int m = Integer.parseInt(parts[0]);
                    int s = Integer.parseInt(parts[1]);
                    return m * 60 + s;
                })
                .sum();

        int m = totalSeconds / 60;
        int s = totalSeconds % 60;
        this.totalDuration = String.format("%d:%02d", m, s);
    }

    @PrePersist
    @PreUpdate
    public void recalcBeforeSave() {
        updateTotalDuration();
    }

    // Convenience helper
    public void addSong(Song s) {
        songs.add(s);
        s.setAlbum(this);
    }

}
