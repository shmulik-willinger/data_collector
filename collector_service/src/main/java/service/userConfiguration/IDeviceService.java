package service.userConfiguration;

import data.DevicePropertiesDTO;
import data.DevicePropertiesRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public interface IDeviceService
{
    ResponseEntity<List<DevicePropertiesDTO>> listAllDevicePropertiesDTOs();

    ResponseEntity<?> getDevicePropertiesDTO(@PathVariable("id") String id);

    ResponseEntity<?> createDevicePropertiesDTO(@RequestBody DevicePropertiesRequest devicePropertiesRequest, UriComponentsBuilder ucBuilder);

    ResponseEntity<?> updateDevicePropertiesDTO(@PathVariable("id") String id, @RequestBody DevicePropertiesRequest devicePropertiesRequest);

    ResponseEntity<?> deleteDevicePropertiesDTO(@PathVariable("id") String id);

    ResponseEntity<DevicePropertiesDTO> deleteAllDevicePropertiesDTOs();
}
