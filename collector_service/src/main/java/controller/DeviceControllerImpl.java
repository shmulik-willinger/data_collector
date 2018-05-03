package controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import data.DevicePropertiesRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;
import service.userConfiguration.IDeviceService;

import java.util.List;

@RestController
@JsonIgnoreProperties(ignoreUnknown = true)
public class DeviceControllerImpl implements IDeviceController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private IDeviceService deviceService;

	// -------------------Retrieve All Devices---------------------------------------------

	@RequestMapping(value = "/device/", method = RequestMethod.GET)
	public ResponseEntity<List<DevicePropertiesRequest>> listAlldevices()
	{
		List<DevicePropertiesRequest> devices = (List<DevicePropertiesRequest>) deviceService.listAllDevicePropertiesDTOs();
		if (devices.isEmpty())
			return new ResponseEntity( HttpStatus.NO_CONTENT);

		return new ResponseEntity<List<DevicePropertiesRequest>>(devices, HttpStatus.OK);
	}

	// -------------------Retrieve Single device------------------------------------------

	@RequestMapping(value = "/device/{id}", method = RequestMethod.GET)
	public ResponseEntity<?> getDevice(@PathVariable("id") long id)
	{
		logger.info("Fetching device with id {}", id);
		ResponseEntity<?> response = deviceService.getDevicePropertiesDTO( String.valueOf( id ) );
		return response;
	}

	// -------------------Create a device-------------------------------------------

	@RequestMapping(value = "/device/", method = RequestMethod.POST)
	public ResponseEntity<?> createDevice(@RequestBody DevicePropertiesRequest devicePropertiesRequest, UriComponentsBuilder ucBuilder)
	{
		ResponseEntity<?> response = deviceService.createDevicePropertiesDTO(devicePropertiesRequest, ucBuilder);
		return response;
	}

	// ------------------- Update a device ------------------------------------------------

	@RequestMapping(value = "/device/{id}", method = RequestMethod.PUT)
	public ResponseEntity<?> updateDevice(@PathVariable("id") long id, @RequestBody DevicePropertiesRequest devicePropertiesRequest)
	{
		logger.info("Updating device with id {}", id);

		ResponseEntity<?> response = deviceService.updateDevicePropertiesDTO( String.valueOf( id ), devicePropertiesRequest );
		return response;
	}

	// ------------------- Delete a device-----------------------------------------

	@RequestMapping(value = "/device/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteDevice(@PathVariable("id") long id)
	{
		ResponseEntity<?> response = deviceService.deleteDevicePropertiesDTO( String.valueOf( id ) );
		return response;
	}

	// ------------------- Delete All devices-----------------------------

	@RequestMapping(value = "/device/", method = RequestMethod.DELETE)
	public ResponseEntity<?> deleteAllDevices()
	{
		ResponseEntity<?> response = deviceService.deleteAllDevicePropertiesDTOs();
		return response;
	}

}
