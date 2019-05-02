package cf.thegc.bugatti;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
@EnableConfigurationProperties(DatasourceProperties.class)
public class BugattiApplication {

    public static void main(String[] args) {
        SpringApplication.run(BugattiApplication.class, args);
    }

}
