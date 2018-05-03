package service.userConfiguration;

import data.DevicePropertiesDTO;
import data.DevicePropertiesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;
import repositories.DevicePropertiesRepository;

import java.util.List;


@Service
public class DeviceService implements IDeviceService {

	@Autowired
	private DevicePropertiesRepository devicePropertiesRepository;

	private static final Logger logger = LoggerFactory.getLogger( DeviceService.class);



	// -------------------Retrieve All DevicePropertiesDTOs---------------------------------------------

	@Override
	public ResponseEntity<List<DevicePropertiesDTO>> listAllDevicePropertiesDTOs() {
		List<DevicePropertiesDTO> DevicePropertiesDTOs = devicePropertiesRepository.findAll();
		if (DevicePropertiesDTOs.isEmpty()) {
			return new ResponseEntity( HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<List<DevicePropertiesDTO>>(DevicePropertiesDTOs, HttpStatus.OK);
	}

	// -------------------Retrieve Single DevicePropertiesDTO------------------------------------------

	@Override
	public ResponseEntity<?> getDevicePropertiesDTO(@PathVariable("id") String id) {
		logger.info("Fetching DevicePropertiesDTO with id {}", id);
		DevicePropertiesDTO devicePropertiesDTO = devicePropertiesRepository.findOne( String.valueOf( id ) );
		if (devicePropertiesDTO == null) {
			logger.error("DevicePropertiesDTO with id {} not found.", id);
			return new ResponseEntity("DevicePropertiesDTO with id " + id + " not found", HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<DevicePropertiesDTO>(devicePropertiesDTO, HttpStatus.OK);
	}

	// -------------------Create a DevicePropertiesDTO-------------------------------------------

	@Override
	public ResponseEntity<?> createDevicePropertiesDTO(@RequestBody DevicePropertiesRequest devicePropertiesRequest, UriComponentsBuilder ucBuilder) {
		logger.info("Creating DevicePropertiesDTO : {}", devicePropertiesRequest);

		DevicePropertiesDTO devicePropertiesDTO = new DevicePropertiesDTO(devicePropertiesRequest);

		if (devicePropertiesRepository.exists( devicePropertiesDTO.getIp())) {
			logger.error("Unable to create. A Device Properties with ip {} already exist", devicePropertiesDTO.getIp());
			return new ResponseEntity("Unable to create. A DevicePropertiesDTO with name " +
					devicePropertiesDTO.getIp() + " already exist.",HttpStatus.CONFLICT);
		}
		devicePropertiesRepository.save(devicePropertiesDTO);

		logger.info(">> Ending addDevice for {}", devicePropertiesDTO.getIp());

		return new ResponseEntity<String>(devicePropertiesDTO.getIp(), HttpStatus.CREATED);
	}

	// ------------------- Update a DevicePropertiesDTO ------------------------------------------------

	@Override
	public ResponseEntity<?> updateDevicePropertiesDTO(@PathVariable("id") String id, @RequestBody DevicePropertiesRequest devicePropertiesRequest) {
		logger.info("Updating DevicePropertiesDTO with id {}", id);

		DevicePropertiesDTO devicePropertiesDTO = new DevicePropertiesDTO(devicePropertiesRequest);
		DevicePropertiesDTO origDevice = devicePropertiesRepository.findOne( String.valueOf( id ) );

		if (origDevice == null) {
			logger.error("Unable to update. DevicePropertiesDTO with id {} not found.", id);
			return new ResponseEntity("Unable to upate. DeviceProperties with id " + id + " not found.", HttpStatus.NOT_FOUND);
		}

		origDevice.setPort(devicePropertiesDTO.getPort());
		origDevice.setType(devicePropertiesDTO.getType());
		origDevice.setVersion(devicePropertiesDTO.getVersion());

		return new ResponseEntity<DevicePropertiesDTO>(devicePropertiesDTO, HttpStatus.OK);
	}

	// ------------------- Delete a DevicePropertiesDTO-----------------------------------------

	@Override
	public ResponseEntity<?> deleteDevicePropertiesDTO(@PathVariable("id") String id) {
		logger.info("Fetching & Deleting DevicePropertiesDTO with id {}", id);

		DevicePropertiesDTO devicePropertiesDTO = devicePropertiesRepository.findOne( String.valueOf( id ) );
		if (devicePropertiesDTO == null) {
			logger.error("Unable to delete. DevicePropertiesDTO with id {} not found.", id);
			return new ResponseEntity("Unable to delete. DevicePropertiesDTO with id " + id + " not found.",
					HttpStatus.NOT_FOUND);
		}
		devicePropertiesRepository.delete( String.valueOf( id ) );
		return new ResponseEntity<DevicePropertiesDTO>(HttpStatus.NO_CONTENT);
	}

	// ------------------- Delete All DevicePropertiesDTOs-----------------------------

	@Override
	public ResponseEntity<DevicePropertiesDTO> deleteAllDevicePropertiesDTOs() {
		logger.info("Deleting All DevicePropertiesDTOs");

		devicePropertiesRepository.deleteAll();
		return new ResponseEntity<DevicePropertiesDTO>( HttpStatus.NO_CONTENT);
	}


}
