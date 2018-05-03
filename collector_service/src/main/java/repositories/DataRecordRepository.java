package repositories;

import data.DataRecordDTO;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface DataRecordRepository extends MongoRepository<DataRecordDTO, String> {

	long count();

}
