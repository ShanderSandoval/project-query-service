package yps.systems.ai.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import yps.systems.ai.model.Project;

@Repository
public interface IProjectRepository extends MongoRepository<Project, String> {
}
