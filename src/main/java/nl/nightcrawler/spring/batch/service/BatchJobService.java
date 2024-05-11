package nl.nightcrawler.spring.batch.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class BatchJobService {

    private final JobLauncher jobLauncher;
    private final Job job;

    public JobExecution run(final String inputFile) throws Exception {
        var jobParameters = new JobParametersBuilder()
                .addLocalDateTime("process_time", LocalDateTime.now())
                .addString("inputFile", inputFile)
                .toJobParameters();
        var jobExecution = jobLauncher.run(job, jobParameters);
        while (jobExecution.isRunning()) {
            log.info("still processing...");
        }
        return jobExecution;
    }


}
