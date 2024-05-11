package nl.nightcrawler.spring.batch.config.employee;

import lombok.AllArgsConstructor;
import nl.nightcrawler.spring.batch.domain.Employee;
import nl.nightcrawler.spring.batch.dto.EmployeeDto;
import nl.nightcrawler.spring.batch.mapper.EmployeeMapper;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@AllArgsConstructor
public class CsvItemProcessor implements ItemProcessor<EmployeeDto, Employee> {
    private final EmployeeMapper employeeMapper;

    @Override
    public Employee process(final EmployeeDto employeeDto) {
        return employeeMapper.toVisitors(employeeDto);
    }
}
