package service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.io.IOException;

@EnableScheduling
@Service
@Slf4j
public class Scheduler
{
    private static final int DEFAULT_UPDATE_TIME_INTERVAL = 5000; // TODO - Externalize as a configuration parameter
    static SNMPAgent agent = null;

    public Scheduler()
    {
        try {
            agent = new SNMPAgent("0.0.0.0/2018");
            agent.start();
        }
        catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Scheduled(fixedDelay = DEFAULT_UPDATE_TIME_INTERVAL)
    public void generateSync()
    {
        System.out.println(">> Starting Scheduler");
        agent.updateValues();
        System.out.println("<< Scheduler simulator finished");
    }
}
