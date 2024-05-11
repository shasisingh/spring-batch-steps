package nl.nightcrawler.spring.batch.dto;

import lombok.Builder;

@Builder
public record EmployeeDto(
        Long employeeId,
        String firstName,
        String lastName,
        String emailAddress,
        String phoneNumber,
        String address,
        String dateOfJoiningDate) {
}
