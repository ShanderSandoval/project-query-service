package yps.systems.ai.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.List;

@Document("project_collection")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@JsonSerialize
@JsonDeserialize
public class Project {

    @Id
    @JsonAlias({"id", "elementId"})
    private String id;

    private String name;

    private String description;

    private LocalDate creationDate;

    private String leaderElementId;

    private String teamElementId;

    private List<String> objectiveElementId;

    private List<String> taskElementId;

}
