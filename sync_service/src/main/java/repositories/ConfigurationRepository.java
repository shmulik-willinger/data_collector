package repositories;

import data.ConfigurationProperties;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ConfigurationRepository extends MongoRepository<ConfigurationProperties, String> {

	ConfigurationProperties findByKey(String key);
	
	Long deleteByKey(ConfigurationProperties key);

	long count();
}
