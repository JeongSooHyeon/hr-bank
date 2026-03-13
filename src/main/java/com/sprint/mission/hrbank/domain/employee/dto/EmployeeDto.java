package com.sprint.mission.hrbank.domain.employee.dto;

import java.time.LocalDate;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record EmployeeDto(
    Long id,
    String name,
    String email,
    String employeeNumber,
    Long departmentId,
    String departmentName,
    String position,

    @DateTimeFormat(iso = ISO.DATE) //YYYY-MM-DD 형식의 포맷으로 자동 파싱됨.
    LocalDate hireDate,
    
    String status,
    Long profileImageId
) {

}
