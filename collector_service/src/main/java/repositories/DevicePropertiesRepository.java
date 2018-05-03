package repositories;

import org.springframework.data.mongodb.repository.MongoRepository;


import data.DevicePropertiesDTO;


public interface DevicePropertiesRepository extends MongoRepository<DevicePropertiesDTO, String> {
	long count();
}
