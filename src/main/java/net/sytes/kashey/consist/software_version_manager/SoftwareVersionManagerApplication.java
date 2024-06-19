package net.sytes.kashey.consist.software_version_manager;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan
public class SoftwareVersionManagerApplication {

    public static void main(String[] args) {
        SpringApplication.run(SoftwareVersionManagerApplication.class, args);
    }

}
