package service.userConfiguration;

import data.CollectableEntityDTO;
import data.CollectableEntityRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;
import repositories.CollectableEntitiesRepository;

import java.util.List;


@Service
public class CollectableEntityService implements ICollectableEntityService {

	@Autowired
	private CollectableEntitiesRepository collectableEntityRepository;

	private static final Logger logger = LoggerFactory.getLogger( CollectableEntityService.class);


	// -------------------Retrieve All CollectableEntityDTOs---------------------------------------------

	@Override
	public ResponseEntity<List<CollectableEntityDTO>> listAllCollectableEntityDTOs() {
		List<CollectableEntityDTO> CollectableEntityDTOs = collectableEntityRepository.findAll();
		if (CollectableEntityDTOs.isEmpty()) {
			return new ResponseEntity( HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<CollectableEntityDTO>>(CollectableEntityDTOs, HttpStatus.OK);
	}

	// -------------------Retrieve Single CollectableEntityDTO------------------------------------------

	@Override
	public ResponseEntity<?> getCollectableEntityDTO(@PathVariable("id") String id) {
		logger.info("Fetching CollectableEntityDTO with id {}", id);
		CollectableEntityDTO collectableEntityDTO = collectableEntityRepository.findOne( String.valueOf( id ) );
		if (collectableEntityDTO == null) {
			logger.error("CollectableEntityDTO with id {} not found.", id);
			return new ResponseEntity("CollectableEntityDTO with id " + id + " not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<CollectableEntityDTO>(collectableEntityDTO, HttpStatus.OK);
	}

	// -------------------Create a CollectableEntityDTO-------------------------------------------

	@Override
	public ResponseEntity<?> createCollectableEntityDTO(@RequestBody CollectableEntityRequest collectableEntityRequest, UriComponentsBuilder ucBuilder) {
		logger.info("Creating CollectableEntityDTO : {}", collectableEntityRequest);

		CollectableEntityDTO collectableEntityDTO = new CollectableEntityDTO(collectableEntityRequest);

		if (collectableEntityRepository.exists( collectableEntityDTO.getEntityOID())) {
			logger.error("Unable to create. A CollectableEntity Properties with ip {} already exist", collectableEntityDTO.getEntityOID());
			return new ResponseEntity("Unable to create. A CollectableEntityDTO with name " +
					collectableEntityDTO.getEntityOID() + " already exist.",HttpStatus.CONFLICT);
		}
		collectableEntityRepository.save(collectableEntityDTO);

		logger.info(">> Ending addCollectableEntity for {}", collectableEntityDTO.getEntityOID());

		return new ResponseEntity<String>(collectableEntityDTO.getEntityOID(), HttpStatus.CREATED);
	}

	// ------------------- Update a CollectableEntityDTO ------------------------------------------------

	@Override
	public ResponseEntity<?> updateCollectableEntityDTO(@PathVariable("id") String id, @RequestBody CollectableEntityRequest collectableEntityRequest) {
		logger.info("Updating CollectableEntityDTO with id {}", id);

		CollectableEntityDTO collectableEntityDTO = new CollectableEntityDTO(collectableEntityRequest);
		CollectableEntityDTO origCollectableEntity = collectableEntityRepository.findOne( String.valueOf( id ) );

		if (origCollectableEntity == null) {
			logger.error("Unable to update. CollectableEntityDTO with id {} not found.", id);
			return new ResponseEntity("Unable to upate. CollectableEntity with id " + id + " not found.", HttpStatus.NOT_FOUND);
		}

		origCollectableEntity.setEntityName(collectableEntityDTO.getEntityName());
		origCollectableEntity.setType(collectableEntityDTO.getType());
		origCollectableEntity.setVersion(collectableEntityDTO.getVersion());

		return new ResponseEntity<CollectableEntityDTO>(collectableEntityDTO, HttpStatus.OK);
	}

	// ------------------- Delete a CollectableEntityDTO-----------------------------------------

	@Override
	public ResponseEntity<?> deleteCollectableEntityDTO(@PathVariable("id") String id) {
		logger.info("Fetching & Deleting CollectableEntityDTO with id {}", id);

		CollectableEntityDTO collectableEntityDTO = collectableEntityRepository.findOne( String.valueOf( id ) );
		if (collectableEntityDTO == null) {
			logger.error("Unable to delete. CollectableEntityDTO with id {} not found.", id);
			return new ResponseEntity("Unable to delete. CollectableEntityDTO with id " + id + " not found.",
					HttpStatus.NOT_FOUND);
		}
		collectableEntityRepository.delete( String.valueOf( id ) );
		return new ResponseEntity<CollectableEntityDTO>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All CollectableEntityDTOs-----------------------------

	@Override
	public ResponseEntity<CollectableEntityDTO> deleteAllCollectableEntitiesDTOs() {
		logger.info("Deleting All CollectableEntityDTOs");

		collectableEntityRepository.deleteAll();
		return new ResponseEntity<CollectableEntityDTO>( HttpStatus.NO_CONTENT);
	}
}
