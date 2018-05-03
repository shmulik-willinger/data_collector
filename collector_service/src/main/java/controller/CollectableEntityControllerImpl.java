package controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import data.CollectableEntityRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import service.userConfiguration.ICollectableEntityService;

import java.util.List;

@RestController
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectableEntityControllerImpl implements ICollectableEntityController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ICollectableEntityService CollectableEntityService;

	// -------------------Retrieve All CollectableEntitys---------------------------------------------

	@RequestMapping(value = "/CollectableEntity/", method = RequestMethod.GET)
	public ResponseEntity<List<CollectableEntityRequest>> listAllCollectableEntities()
	{
		List<CollectableEntityRequest> CollectableEntities = (List<CollectableEntityRequest>) CollectableEntityService.listAllCollectableEntityDTOs();
		if (CollectableEntities.isEmpty())
			return new ResponseEntity( HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<CollectableEntityRequest>>(CollectableEntities, HttpStatus.OK);
	}

	// -------------------Retrieve Single CollectableEntity------------------------------------------

	@RequestMapping(value = "/CollectableEntity/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getCollectableEntity(@PathVariable("id") long id)
	{
		logger.info("Fetching CollectableEntity with id {}", id);
		ResponseEntity<?> response = CollectableEntityService.getCollectableEntityDTO( String.valueOf( id ) );
		return response;
	}

	// -------------------Create a CollectableEntity-------------------------------------------

	@RequestMapping(value = "/CollectableEntity/", method = RequestMethod.POST)
	public ResponseEntity<?> createCollectableEntity(@RequestBody CollectableEntityRequest CollectableEntityPropertiesRequest, UriComponentsBuilder ucBuilder)
	{
		ResponseEntity<?> response = CollectableEntityService.createCollectableEntityDTO(CollectableEntityPropertiesRequest, ucBuilder);
		return response;
	}

	// ------------------- Update a CollectableEntity ------------------------------------------------

	@RequestMapping(value = "/CollectableEntity/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateCollectableEntity(@PathVariable("id") long id, @RequestBody CollectableEntityRequest CollectableEntityPropertiesRequest)
	{
		logger.info("Updating CollectableEntity with id {}", id);

		ResponseEntity<?> response = CollectableEntityService.updateCollectableEntityDTO( String.valueOf( id ), CollectableEntityPropertiesRequest );
		return response;
	}

	// ------------------- Delete a CollectableEntity-----------------------------------------

	@RequestMapping(value = "/CollectableEntity/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteCollectableEntity(@PathVariable("id") long id)
	{
		ResponseEntity<?> response = CollectableEntityService.deleteCollectableEntityDTO( String.valueOf( id ) );
		return response;
	}

	// ------------------- Delete All CollectableEntities-----------------------------

	@RequestMapping(value = "/CollectableEntity/", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAllCollectableEntities()
	{
		ResponseEntity<?> response = CollectableEntityService.deleteAllCollectableEntitiesDTOs();
		return response;
	}

}
