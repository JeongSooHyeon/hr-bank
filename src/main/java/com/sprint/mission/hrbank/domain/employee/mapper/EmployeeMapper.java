package com.sprint.mission.hrbank.domain.employee.mapper;

import com.sprint.mission.hrbank.domain.employee.Employee;
import com.sprint.mission.hrbank.domain.employee.dto.CursorPageResponseEmployeeDto;
import com.sprint.mission.hrbank.domain.employee.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    // Employee -> dto

    @Mapping(target = "departmentId", source = "department.id")
    @Mapping(target = "departmentName", source = "department.name")
    @Mapping(target = "status", expression = "java(employee.getStatus() != null ? employee.getStatus().name() : null)")
    @Mapping(target = "profileImageId", ignore = true)
        // 구조 확정 전 임시

    EmployeeDto entityToDto(Employee employee);

    // dto -> Employee
    Employee dtoToEntity(EmployeeDto employeeDto);

    // Employee -> CursorDto
    CursorPageResponseEmployeeDto entityToCursorResponseDto(Employee employee);
}
