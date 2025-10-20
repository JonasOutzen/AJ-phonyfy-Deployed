package app.entities;

import app.dtos.SampleDTO;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class Sample {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @Column(name="user_name", length=100, nullable = false)
    private String userName;

    @Column(name="sample_number", nullable = false)
    private Integer sampleNumber;

    public Sample(SampleDTO dto) {
        this.id = dto.getId();
        this.userName = dto.getUserName();
        this.sampleNumber = dto.getSampleNumber();
    }

}

