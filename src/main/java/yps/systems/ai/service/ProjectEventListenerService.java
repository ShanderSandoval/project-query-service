package yps.systems.ai.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Service;
import yps.systems.ai.model.Project;
import yps.systems.ai.object.ProjectObjective;
import yps.systems.ai.object.ProjectPerson;
import yps.systems.ai.object.ProjectTask;
import yps.systems.ai.object.ProjectTeam;
import yps.systems.ai.repository.IProjectRepository;

import java.util.Optional;

@Service
public class ProjectEventListenerService {

    private final IProjectRepository projectRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    public ProjectEventListenerService(IProjectRepository projectRepositor, ObjectMapper objectMappery) {
        this.projectRepository = projectRepository;
        this.objectMapper = objectMapper;
    }

    @KafkaListener(topics = "${env.kafka.topicEvent}")
    public void listen(@Payload String payload, @Header("eventType") String eventType, @Header("source") String source) {
        System.out.println("Processing " + eventType + " event from " + source);
        switch (eventType) {
            case "CREATE_PROJECT":
                try {
                    Project project = objectMapper.readValue(payload, Project.class);
                    projectRepository.save(project);
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "SET_TEAM":
                try {
                    ProjectTeam projectTeam = objectMapper.readValue(payload, ProjectTeam.class);
                    Optional<Project> optionalProject = projectRepository.findById(projectTeam.projectElementId());
                    optionalProject.ifPresent(project -> {
                        project.setTeamElementId(projectTeam.teamElementId());
                        projectRepository.save(project);
                    });
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "SET_TUTOR":
                try {
                    ProjectPerson projectPerson = objectMapper.readValue(payload, ProjectPerson.class);
                    Optional<Project> optionalProject = projectRepository.findById(projectPerson.projectElementId());
                    optionalProject.ifPresent(project -> {
                        project.setLeaderElementId(projectPerson.personElementId());
                        projectRepository.save(project);
                    });
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "SET_OBJECTIVE":
                try {
                    ProjectObjective projectObjective = objectMapper.readValue(payload, ProjectObjective.class);
                    Optional<Project> optionalProject = projectRepository.findById(projectObjective.projectElementId());
                    optionalProject.ifPresent(project -> {
                        project.getObjectiveElementId().add(projectObjective.objectiveElementId());
                        projectRepository.save(project);
                    });
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "SET_TASK":
                try {
                    ProjectTask projectTask = objectMapper.readValue(payload, ProjectTask.class);
                    Optional<Project> optionalProject = projectRepository.findById(projectTask.projectElementId());
                    optionalProject.ifPresent(project -> {
                        project.getTaskElementId().add(projectTask.taskElementId());
                        projectRepository.save(project);
                    });
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "UPDATE_PROJECT":
                try {
                    Project project = objectMapper.readValue(payload, Project.class);
                    Optional<Project> optionalProject = projectRepository.findById(project.getId());
                    optionalProject.ifPresent(existingRole -> projectRepository.save(project));
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "DELETE_PROJECT":
                Optional<Project> optionalProject = projectRepository.findById(payload.replaceAll("\"", ""));
                optionalProject.ifPresent(value -> projectRepository.deleteById(value.getId()));
                break;
            case "REMOVE_TEAM":
                try {
                    ProjectTeam projectTeam = objectMapper.readValue(payload, ProjectTeam.class);
                    Optional<Project> projectOptional = projectRepository.findById(projectTeam.projectElementId());
                    projectOptional.ifPresent(project -> {
                        project.setTeamElementId(null);
                        projectRepository.save(project);
                    });
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "REMOVE_TUTOR":
                try {
                    ProjectPerson projectPerson = objectMapper.readValue(payload, ProjectPerson.class);
                    Optional<Project> projectOptional = projectRepository.findById(projectPerson.projectElementId());
                    projectOptional.ifPresent(project -> {
                        project.setLeaderElementId(null);
                        projectRepository.save(project);
                    });
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "REMOVE_OBJECTIVE":
                try {
                    ProjectObjective projectObjective = objectMapper.readValue(payload, ProjectObjective.class);
                    Optional<Project> projectOptional = projectRepository.findById(projectObjective.projectElementId());
                    projectOptional.ifPresent(project -> {
                        project.getObjectiveElementId().remove(projectObjective.objectiveElementId());
                        projectRepository.save(project);
                    });
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            case "REMOVE_TASK":
                try {
                    ProjectTask projectTask = objectMapper.readValue(payload, ProjectTask.class);
                    Optional<Project> projectOptional = projectRepository.findById(projectTask.projectElementId());
                    projectOptional.ifPresent(project -> {
                        project.getTaskElementId().remove(projectTask.taskElementId());
                        projectRepository.save(project);
                    });
                } catch (JsonProcessingException e) {
                    System.err.println("Error parsing Person JSON: " + e.getMessage());
                }
                break;
            default:
                System.out.println("Unknown event type: " + eventType);
        }
    }

}
