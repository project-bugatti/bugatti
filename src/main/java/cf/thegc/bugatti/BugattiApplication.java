package cf.thegc.bugatti;

import org.apache.maven.model.Model;
import org.apache.maven.model.io.xpp3.MavenXpp3Reader;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.io.FileReader;

@SpringBootApplication
@EnableJpaAuditing
public class BugattiApplication {

    public static String APPLICATION_VERSION;

    public static void main(String[] args) {
        SpringApplication.run(BugattiApplication.class, args);
    }


    /**
     * On application startup, this method fetches the application version and assigns the value to a static variable
     * The application version is used as a label for logs
     */
    @EventListener(ApplicationReadyEvent.class)
    public void fetchVersionFromPomForLogging() {
        try {
            MavenXpp3Reader reader = new MavenXpp3Reader();
            Model model = reader.read(new FileReader("pom.xml"));
            APPLICATION_VERSION = model.getVersion();
        } catch (Exception e) {
            APPLICATION_VERSION = "null";
        }
    }
}
