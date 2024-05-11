package nl.nightcrawler.spring.batch.config.employee;

import nl.nightcrawler.spring.batch.domain.Employee;
import nl.nightcrawler.spring.batch.dto.EmployeeDto;
import nl.nightcrawler.spring.batch.mapper.EmployeeMapper;
import nl.nightcrawler.spring.batch.repositories.EmployeeRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.LineMapper;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
public class BatchConfig {

    private static final String[] EMPLOYEE_PARAMETERS = {"employeeId", "firstName", "lastName", "emailAddress", "phoneNumber", "address", "dateOfJoiningDate"};

    @Bean
    public Job employeeJob(final JobRepository jobRepository, final PlatformTransactionManager transactionManager, final EmployeeRepository employeeRepository, final EmployeeMapper employeeMapper) {
        return new JobBuilder("readEmployeeCsvJob", jobRepository)
                .incrementer(new RunIdIncrementer())
                .start(employeeJobStep(jobRepository, transactionManager, employeeRepository, employeeMapper))
                .build();
    }

    @Bean
    public Step employeeJobStep(final JobRepository jobRepository, final PlatformTransactionManager transactionManager, final EmployeeRepository employeeRepository, final EmployeeMapper employeeMapper) {
        return new StepBuilder("readEmployeeCsvStep", jobRepository)
                .<EmployeeDto, Employee>chunk(100, transactionManager)
                .reader(flatFileItemReader(null))
                .processor(itemProcessor(employeeMapper))
                .writer(employeeItemWriter(employeeRepository))
                .build();
    }

    @Bean
    public org.springframework.batch.item.ItemProcessor<EmployeeDto, Employee> itemProcessor(final EmployeeMapper employeeMapper) {
        return new CsvItemProcessor(employeeMapper);
    }

    @Bean
    public CsvItemWriter employeeItemWriter(final EmployeeRepository employeeRepository) {
        return new CsvItemWriter(employeeRepository);
    }


    @Bean
    @StepScope
    public FlatFileItemReader<EmployeeDto> flatFileItemReader(@Value("#{jobParameters['inputFile']}") final String employeeImportFile) {
        var flatFileItemReader = new FlatFileItemReader<EmployeeDto>();
        flatFileItemReader.setName("EMPLOYEE_CSV_READER");
        flatFileItemReader.setLinesToSkip(1);
        flatFileItemReader.setLineMapper(csvMapper());
        flatFileItemReader.setStrict(false);
        flatFileItemReader.setResource(new FileSystemResource(employeeImportFile));
        return flatFileItemReader;
    }

    @Bean
    public LineMapper<EmployeeDto> csvMapper() {
        var defaultLineMapper = new DefaultLineMapper<EmployeeDto>();
        var lineTokenizer = new DelimitedLineTokenizer();
        lineTokenizer.setDelimiter(",");
        lineTokenizer.setNames(EMPLOYEE_PARAMETERS);
        lineTokenizer.setStrict(false);
        defaultLineMapper.setLineTokenizer(lineTokenizer);
        defaultLineMapper.setFieldSetMapper(new CsvFieldSetMapper());
        return defaultLineMapper;

    }

}
