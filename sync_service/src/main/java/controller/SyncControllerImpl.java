package controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import service.ISyncService;

@RestController
public abstract class SyncControllerImpl implements ISyncController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private ISyncService syncService;


	@RequestMapping(value = "/sync/{size}", method = RequestMethod.POST)
	public void generateSync(@PathVariable("size") long size){

		logger.debug(">> generate sync: size={}", size);

		syncService.sync(size);

		logger.debug("<< generateSync return void");
	}
}
