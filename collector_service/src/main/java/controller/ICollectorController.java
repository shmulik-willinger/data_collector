package controller;


import io.swagger.annotations.Api;
import org.springframework.http.ResponseEntity;

@Api(
        value = "Rest Controller to call the collection function",
        tags = {"Collector Controller"}
)
public interface ICollectorController
{
    ResponseEntity<?> collectData();

}
