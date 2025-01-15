package com.parkingservice.parking.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("prod")
public class ProdConfig {

    private static final Logger logger = LogManager.getLogger(ProdConfig.class);
    @Value("${spring.datasource.url}")
    private String datasourceURL;

    @Bean
    public String validateDataSource() {
        if (datasourceURL.contains("localhost") || datasourceURL.contains("127.0.0.1")) {
            logger.error("Prod profile uses localhost database!");
        }
        return "Using production datasource settings";
    }
}
