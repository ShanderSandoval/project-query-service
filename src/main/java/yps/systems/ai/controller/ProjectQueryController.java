package yps.systems.ai.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import yps.systems.ai.model.Project;
import yps.systems.ai.repository.IProjectRepository;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/query/projectService")
public class ProjectQueryController {

    private final IProjectRepository projectRepository;

    @Autowired
    public ProjectQueryController(IProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    @GetMapping
    public ResponseEntity<List<Project>> getPersons() {
        List<Project> projects = projectRepository.findAll();
        return ResponseEntity.ok(projects);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Project> getPersonById(@PathVariable String id) {
        Optional<Project> optionalProject = projectRepository.findById(id);
        return optionalProject.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

}
