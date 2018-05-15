package service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@EnableScheduling
@Service
public class Scheduler
{
    private static final int DEFAULT_COLLECTION_TIME_INTERVAL = 10000; // TODO - Externalize as a configuration parameter
    private final Logger logger = LoggerFactory.getLogger(getClass());
    private static final int DEFAULT_COLLECT_SIZE= 10; //  TODO - Externalize as a configuration parameter

    @Autowired
    private ISyncService syncService;

    @Scheduled(fixedDelay = DEFAULT_COLLECTION_TIME_INTERVAL)
    public void generateSync()
    {
        logger.debug(">> Starting Scheduler");

        //syncService.sparkPOC();
        syncService.sync(DEFAULT_COLLECT_SIZE);

        logger.debug("<< Scheduler sync finished");
    }
}
