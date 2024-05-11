package nl.nightcrawler.spring.batch;

import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableBatchProcessing
@EnableScheduling
public class BatchApplication {

    public static void main(String[] args) {
        new SpringApplicationBuilder(BatchApplication.class)
                .web(WebApplicationType.SERVLET)
                .run(args)
                .registerShutdownHook();
    }

}
