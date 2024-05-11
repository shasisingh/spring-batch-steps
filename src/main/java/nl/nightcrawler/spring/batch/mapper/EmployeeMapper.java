package nl.nightcrawler.spring.batch.mapper;

import nl.nightcrawler.spring.batch.domain.Employee;
import nl.nightcrawler.spring.batch.dto.EmployeeDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {
    Employee toVisitors(EmployeeDto employeeDto);
}
