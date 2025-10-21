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
public class SampleDTO {

    private Integer id;
    private String userName;
    private Integer sampleNumber;

    public SampleDTO(Artist sample) {
        this.id = sample.getId();
        this.userName = sample.getUserName();
        this.sampleNumber = sample.getSampleNumber();
    }

    public SampleDTO(Integer id, String userName, Integer sampleNumber) {
        this.id = id;
        this.userName = userName;
        this.sampleNumber = sampleNumber;
    }

    public static List<SampleDTO> toDTOList(List<Artist> resultList) {
        return resultList.stream().map(SampleDTO::new).toList();
    }
}
