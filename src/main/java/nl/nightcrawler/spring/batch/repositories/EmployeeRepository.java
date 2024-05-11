package nl.nightcrawler.spring.batch.repositories;

import nl.nightcrawler.spring.batch.domain.Employee;
import org.springframework.data.repository.ListCrudRepository;

public interface EmployeeRepository extends ListCrudRepository<Employee, Long> {
}
