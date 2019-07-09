package cf.thegc.bugatti;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class BugattiApplication {

    public static String APPLICATION_NAME;
    public static String APPLICATION_VERSION;

    @Autowired
    private BuildProperties buildProperties;

    public static void main(String[] args) {
        SpringApplication.run(BugattiApplication.class, args);
    }


    /**
     * On application startup, the application version from pom.xml is assigned to a global variable
     * The application version is used as a label for logs
     */
    @EventListener(ApplicationReadyEvent.class)
    public void fetchApplicationVersion() {
        APPLICATION_NAME = buildProperties.getName();
        APPLICATION_VERSION = buildProperties.getVersion();
    }
}
