package cf.thegc.bugatti;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@ConfigurationProperties(prefix = "spring.datasource")
@PropertySource(value = "classpath:datasource.yml")
public class DatasourceProperties {
}
