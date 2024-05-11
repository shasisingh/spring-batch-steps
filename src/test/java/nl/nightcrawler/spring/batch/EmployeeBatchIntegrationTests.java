package nl.nightcrawler.spring.batch;


import nl.nightcrawler.spring.batch.config.employee.BatchConfig;
import nl.nightcrawler.spring.batch.repositories.EmployeeRepository;
import lombok.val;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.batch.core.ExitStatus;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersBuilder;
import org.springframework.batch.test.JobLauncherTestUtils;
import org.springframework.batch.test.JobRepositoryTestUtils;
import org.springframework.batch.test.context.SpringBatchTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.time.LocalDateTime;


@SpringBatchTest
@SpringJUnitConfig(classes = {BatchConfig.class, BatchTestConfig.class})
class EmployeeBatchIntegrationTests {

    public static final String INPUT_FILE = "src/test/resources/employee.csv";
    @Autowired
    private JobLauncherTestUtils jobLauncherTestUtils;
    @Autowired
    private JobRepositoryTestUtils jobRepositoryTestUtils;

    @AfterEach
    public void cleanUp() {
        jobRepositoryTestUtils.removeJobExecutions();
    }

    @Test
    void testJobUtils(@Autowired EmployeeRepository employeeRepository) throws Exception {
        val jobExecution = jobLauncherTestUtils.launchJob(defaultJobParameters());
        val actualJobInstance = jobExecution.getJobInstance();
        val actualJobExitStatus = jobExecution.getExitStatus();

        Assertions.assertThat(actualJobInstance.getJobName()).isEqualTo("readEmployeeCsvJob");
        Assertions.assertThat(actualJobExitStatus.getExitCode()).isEqualTo(ExitStatus.COMPLETED.getExitCode());
        Assertions.assertThat(employeeRepository.findAll()).hasSize(2);

    }


    private JobParameters defaultJobParameters() {
        var paramsBuilder = new JobParametersBuilder();
        paramsBuilder.addString("inputFile", INPUT_FILE);
        paramsBuilder.addLocalDateTime("processing_time", LocalDateTime.now());
        return paramsBuilder.toJobParameters();
    }

}
