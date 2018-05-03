package service.collectingData;

import data.CollectableEntityDTO;
import exceptions.CollectionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import repositories.Dao;
import utils.CollectionProtocol;

import java.util.Arrays;
import java.util.Date;

@EnableScheduling
@Service
public class CollectionScheduler {

	private static final int DEFAULT_COLLECTION_TIME_INTERVAL = 30000; // 30 seconds  TODO - Externalize as a configuration parameter

	private static final Logger logger = LoggerFactory.getLogger(CollectionScheduler.class);

	@Autowired
	private CollectorFactory factory;

	@Autowired
	private Dao dao;

	
	private 	long updatedNum;
	private static int launchCounter;
	/**
//	 * The scheduled method to start collection
	 */
	@Scheduled(fixedDelay = DEFAULT_COLLECTION_TIME_INTERVAL)
	public void scheduledCollection() {
		logger.info("scheduleCollection start");
		
		updatedNum=0;

		Arrays.asList( CollectionProtocol.values()).stream().forEach( protocol -> {
			try {
				factory.getCollector(protocol).init();
			} catch (UnsupportedOperationException | CollectionException e1) {
				logger.error("Can't init collector for protocol " + protocol + ": " + e1.getMessage());
			}
		});

		Date date1=new Date();
		Arrays.asList(CollectionProtocol.values()).parallelStream().forEach(protocol -> {
			try {
				updatedNum+=factory.getCollector(protocol).collectAll();
			} catch (UnsupportedOperationException e) {
				logger.error("Can't collectAll: " + e.getMessage());
			}
		});
		launchCounter++;
		Date date2=new Date();
		long diffSec=(date2.getTime()-date1.getTime())/1000;
		if (diffSec==0)
			diffSec=1;
		logger.info("scheduleCollection end. cycle-"+launchCounter+". elements collected-"+updatedNum+". collection took-" +diffSec+"s. ("+updatedNum/diffSec+" per second)");
	}

	private void prepareCollection(CollectableEntityDTO entity) {
		String uid = entity.getEntityOID();
		logger.debug("Going to prepare " + uid);

		try {
			Collector collector = factory.getCollector(CollectionProtocol.SNMP);
			collector.prepareCollection(entity);
		} catch (Exception e) {
			logger.error("Got exception in prepareCollection for " + uid + ": " + e);

		}
		logger.debug("Finished prepare collecting " + uid);
	}
}
