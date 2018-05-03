package service.userConfiguration;

import data.CollectableEntityDTO;
import data.CollectableEntityRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

public interface ICollectableEntityService
{
    ResponseEntity<List<CollectableEntityDTO>> listAllCollectableEntityDTOs();

    ResponseEntity<?> getCollectableEntityDTO(@PathVariable("id") String id);

    ResponseEntity<?> createCollectableEntityDTO(@RequestBody CollectableEntityRequest collectableEntityRequest, UriComponentsBuilder ucBuilder);

    ResponseEntity<?> updateCollectableEntityDTO(@PathVariable("id") String id, @RequestBody CollectableEntityRequest collectableEntityRequest);

    ResponseEntity<?> deleteCollectableEntityDTO(@PathVariable("id") String id);

    ResponseEntity<CollectableEntityDTO> deleteAllCollectableEntitiesDTOs();
}
