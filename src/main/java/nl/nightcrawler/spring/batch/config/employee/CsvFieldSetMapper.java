package nl.nightcrawler.spring.batch.config.employee;

import nl.nightcrawler.spring.batch.dto.EmployeeDto;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;

public class CsvFieldSetMapper implements FieldSetMapper<EmployeeDto> {

    @Override
    public EmployeeDto mapFieldSet(final FieldSet fieldSet) {
        return EmployeeDto.builder()
                .employeeId(fieldSet.readLong("employeeId"))
                .dateOfJoiningDate(fieldSet.readString("dateOfJoiningDate"))
                .firstName(fieldSet.readString("firstName"))
                .lastName(fieldSet.readString("lastName"))
                .emailAddress(fieldSet.readString("emailAddress"))
                .phoneNumber(fieldSet.readString("phoneNumber"))
                .address(fieldSet.readString("address"))
                .build();
    }
}
