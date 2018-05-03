package repositories;

import data.CollectableEntityDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CollectableEntitiesRepository extends MongoRepository<CollectableEntityDTO, String> {

	long count();

}
