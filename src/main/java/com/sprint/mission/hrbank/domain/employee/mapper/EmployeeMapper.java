package com.sprint.mission.hrbank.domain.employee.mapper;

import com.sprint.mission.hrbank.domain.employee.Employee;
import com.sprint.mission.hrbank.domain.employee.EmployeeStatus;
import com.sprint.mission.hrbank.domain.employee.dto.CursorPageResponseEmployeeDto;
import com.sprint.mission.hrbank.domain.employee.dto.EmployeeDto;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

  // MapStruct가 source=status 매핑 ???�동 ?�용
  default String mapStatus(EmployeeStatus status) {
    return status != null ? status.name() : null;
  }


  // Employee -> dto
  @Mapping(target = "departmentId", source = "department.id")
  @Mapping(target = "departmentName", source = "department.name")
  @Mapping(target = "status", source = "status") // expression ?�거
  @Mapping(target = "profileImageId", source = "profileImage.id")
  // 구조 ?�정 ???�시
  EmployeeDto entityToDto(Employee employee);

  // dto -> Employee
  Employee dtoToEntity(EmployeeDto employeeDto);

  // Employee -> CursorDto
  CursorPageResponseEmployeeDto entityToCursorResponseDto(Employee employee);
}

