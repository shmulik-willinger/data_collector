package controller;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import service.collectingData.CollectionScheduler;

@RestController
@JsonIgnoreProperties(ignoreUnknown = true)
public class CollectorControllerImpl implements ICollectorController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	CollectionScheduler collectorService = new CollectionScheduler();

	@RequestMapping(value = "/collect/", method = RequestMethod.GET)
	public ResponseEntity<?> collectData()
	{
		collectorService.scheduledCollection();
		return new ResponseEntity<>( "Collect Data finished successfully", HttpStatus.OK );
	}
}
