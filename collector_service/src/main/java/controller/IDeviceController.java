package controller;


import data.DevicePropertiesRequest;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Api(
        value = "Rest Controller to manage devices configurations",
        tags = {"Devices Controller"}
)
public interface IDeviceController
{
    ResponseEntity<List<DevicePropertiesRequest>> listAlldevices();

    ResponseEntity<?> getDevice(@PathVariable("id") long id);

    ResponseEntity<?> createDevice(@RequestBody DevicePropertiesRequest devicePropertiesRequest, UriComponentsBuilder ucBuilder);

    ResponseEntity<?> updateDevice(@PathVariable("id") long id, @RequestBody DevicePropertiesRequest devicePropertiesRequest);

    ResponseEntity<?> deleteDevice(@PathVariable("id") long id);

    ResponseEntity<?> deleteAllDevices();


}
