package repositories;

import com.mongodb.WriteResult;
import data.CollectableEntityDTO;
import data.DataRecordDTO;
import data.DevicePropertiesDTO;
import data.ValueRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@EnableMongoRepositories
public class Dao {

	Dao() {

	}

	@Autowired
	private CollectableEntitiesRepository entityRepository;

	@Autowired
	private DataRecordRepository pmRepository;
	
	@Autowired
	private DevicePropertiesRepository deviceCollectionPropertiesRepository;

	@Autowired
	private MongoOperations mongoOps;

	private static final Logger logger = LoggerFactory.getLogger(Dao.class);

	public void writeToDB(CollectableEntityDTO record) {
		logger.debug("Going to save " + record + " on " + entityRepository );
		entityRepository.save(record);
	}

	public DataRecordDTO createPMEntity(String key){
		DataRecordDTO pm = new DataRecordDTO(key);
		return pmRepository.save(pm);
	}


	public List<CollectableEntityDTO> getAllConfigEntities() {
		return entityRepository.findAll();
	}

	public long countConfigEntities() {
		return entityRepository.count();
	}

	public long countPMEntities() {
		return pmRepository.count();
	}

	public void updateEntityPM(String uid, LocalDateTime time, ArrayList<String> value) {
		
		ValueRecord valueRecord = new ValueRecord(time, value);
		logger.debug("port value added. going to save");
		
		WriteResult writeResult=mongoOps.updateFirst(
				new Query( Criteria.where("_id").is(uid)),
				new Update().push("values", valueRecord) , 
				DataRecordDTO.class);
		if (writeResult.getN()==0){// firts time
			DataRecordDTO pm =createPMEntity(uid);
			pm.addValue(valueRecord);
			pmRepository.save(pm);
		}
	}


	public DevicePropertiesDTO writeToDB(DevicePropertiesDTO entity){
		return deviceCollectionPropertiesRepository.save(entity);
	}
	
	public DevicePropertiesDTO getDeviceCollectionPropertiesEntity(String deviceId){
		return deviceCollectionPropertiesRepository.findOne(deviceId);
	}

	public List<DevicePropertiesDTO> getAllDevices() {
		return deviceCollectionPropertiesRepository.findAll();
	}

	public List<CollectableEntityDTO> getAllCollectableEntities()
	{
		return entityRepository.findAll();
	}
}
