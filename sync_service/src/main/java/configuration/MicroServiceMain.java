package configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.SmartLifecycle;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

import java.io.IOException;


@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan({"configuration", "controller", "repositories", "service", "data"})
public class MicroServiceMain implements SmartLifecycle {
	private static final Logger logger = LoggerFactory.getLogger(MicroServiceMain.class);
	private static final Logger activityLogger = LoggerFactory.getLogger("ActivityLogger");
	
	public static void main(String[] args) {
		try {
			
			@SuppressWarnings("unused")
			ConfigurableApplicationContext context = new SpringApplicationBuilder(MicroServiceMain.class).run(args);

			activityLogger.debug("Starting...");
			System.out.println("@@@@@ Sync service is up @@@@@@");
		}
		
		catch (Exception e) {
			logger.error("Failed to start - Exception = " + e.getMessage());
			e.printStackTrace();
		}
	}
  
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertySourcesPlaceholderConfigurer() {
		PropertySourcesPlaceholderConfigurer c = new PropertySourcesPlaceholderConfigurer();
		c.setIgnoreUnresolvablePlaceholders(false);
		c.setIgnoreResourceNotFound(false);
		 try {
			c.setLocations(new PathMatchingResourcePatternResolver().getResources("classpath:/**/*.properties"));
		} catch (IOException e) {
			logger.error("Failed to open file" + e.getMessage());
			e.printStackTrace();
		}
		return c;
	}

	
	//////////////////////////// LIFE CYCLE IMPLEMENTATIONS
	
	@Override
	public void start() {
		System.out.println("@@@@@@@@@@ SPRING Finished starting Application @@@@@@@@@@@@@@");
//		LCareZabbixFileFacade.FILES.initServer();
	}
	@Override
	public void stop() {
		System.out.println("@@@@@@@@@@ Stopping @@@@@@@@@@@@@@");
	}
	@Override
	public void stop(Runnable callback) {
		System.out.println("@@@@@@@ Stopping with callback:" + callback);
	}
	@Override
	public boolean isRunning() { return false; }
	@Override
	public boolean isAutoStartup() { return true; }	
	@Override
	public int getPhase() {	return 0; }
}
