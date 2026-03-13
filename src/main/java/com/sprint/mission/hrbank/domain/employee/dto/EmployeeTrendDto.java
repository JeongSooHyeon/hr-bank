package com.sprint.mission.hrbank.domain.employee.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record EmployeeTrendDto(
    String date,

    Long count,
    Long change,
    Double changeRate) {

}
