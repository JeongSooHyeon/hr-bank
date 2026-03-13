package com.sprint.mission.hrbank.domain.employee.dto;

import java.time.Instant;
import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record EmployeeCreateRequest(
    String name,
    String email,
    Long departmentId,
    String position,

    String hireDate,
    String memo
) {

}
