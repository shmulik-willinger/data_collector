package controller;


import data.CollectableEntityRequest;
import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;

@Api(
        value = "Rest Controller to manage the entities to collect configurations",
        tags = {"CollectableEntity Controller"}
)
public interface ICollectableEntityController
{
    ResponseEntity<List<CollectableEntityRequest>> listAllCollectableEntities();

    ResponseEntity<?> getCollectableEntity(@PathVariable("id") long id);

    ResponseEntity<?> createCollectableEntity(@RequestBody CollectableEntityRequest collectableEntityRequest, UriComponentsBuilder ucBuilder);

    ResponseEntity<?> updateCollectableEntity(@PathVariable("id") long id, @RequestBody CollectableEntityRequest collectableEntityRequest);

    ResponseEntity<?> deleteCollectableEntity(@PathVariable("id") long id);

    ResponseEntity<?> deleteAllCollectableEntities();


}
