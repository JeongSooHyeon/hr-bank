package com.sprint.mission.hrbank.domain.employee.dto;

import com.sprint.mission.hrbank.domain.employee.EmployeeStatus;
import java.time.LocalDate;
import lombok.Builder.Default;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.format.annotation.DateTimeFormat.ISO;

public record EmployeeSearchRequest(String nameOrEmail,
                                    String departmentName,
                                    String position,
                                    String employeeNumber,

                                    String hireDateFrom,

                                    String hireDateTo,

                                    EmployeeStatus status,
                                    String cursor,
                                    Long idAfter,
                                    int size,
                                    String sortField,
                                    String sortDirection) {

}
