package nl.nightcrawler.spring.batch.config.employee;

import lombok.AllArgsConstructor;
import nl.nightcrawler.spring.batch.domain.Employee;
import nl.nightcrawler.spring.batch.repositories.EmployeeRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CsvItemWriter implements ItemWriter<Employee> {

    private final EmployeeRepository employeeRepository;

    @Override
    public void write(Chunk<? extends Employee> visitors) {
        visitors.getItems().forEach(employeeRepository::save);
    }
}
