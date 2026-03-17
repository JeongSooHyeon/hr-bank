package com.sprint.mission.hrbank.domain.employee;

import com.sprint.mission.hrbank.domain.employee.dto.CursorPageResponseEmployeeDto;
import com.sprint.mission.hrbank.domain.employee.dto.EmployeeCountRequest;
import com.sprint.mission.hrbank.domain.employee.dto.EmployeeCreateRequest;
import com.sprint.mission.hrbank.domain.employee.dto.EmployeeDto;
import com.sprint.mission.hrbank.domain.employee.dto.EmployeeSearchRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping(value = "/api/employees")
@RequiredArgsConstructor
@Tag(name = "직원 관리", description = "직원 관리 API")
public class EmployeeController {

  private final EmployeeService employeeService; // 추후 구현 예정

  @GetMapping("/count")
  @Operation(summary = "직원 수 조회", description = "지정된 조건에 맞는 직원 수를 조회합니다. 상태 필터링 및 입사일 기간 필터링이 가능합니다.")
  public ResponseEntity<Long> getEmployeeCount(@ModelAttribute EmployeeCountRequest request) {
    return ResponseEntity.ok(employeeService.getEmployeeCount(request));
  }

  @GetMapping
  public ResponseEntity<CursorPageResponseEmployeeDto> getEmployees(EmployeeSearchRequest req) {
    CursorPageResponseEmployeeDto response = employeeService.getEmployees(req);
    return ResponseEntity.ok(response);
  }

  @PostMapping
  public ResponseEntity<EmployeeDto> createEmployee(@RequestBody EmployeeCreateRequest req,
      @RequestPart MultipartFile profile) {
    return ResponseEntity.ok(employeeService.create(req, profile));
  }

  // id를 pathvariable로 받고 삭제 수행
  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteEmployee(@PathVariable Long id) {
    employeeService.delete(id);
    return ResponseEntity.noContent().build();
  }
}
