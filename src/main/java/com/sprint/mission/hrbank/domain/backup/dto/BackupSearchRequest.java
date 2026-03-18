package com.sprint.mission.hrbank.domain.backup.dto;

import com.sprint.mission.hrbank.domain.backup.BackupStatus;
import java.time.Instant;

// 백업 목록 조회용 dto
public record BackupSearchRequest(
    String worker,
    BackupStatus status,
    Instant startedAtFrom,
    Instant startedAtTo,
    Long idAfter,
    String cursor,
    Integer size,
    String sortField,
    String sortDirection
) {

  public BackupSearchRequest {
    if (size == null || size <= 0) {
      size = 10;
    }
    if (sortField == null || sortField.isBlank()) {
      sortField = "startedAt";
    }
    if (sortDirection == null || sortDirection.isBlank()) {
      sortDirection = "desc";
    }
  }
}
