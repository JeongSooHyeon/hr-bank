package com.sprint.mission.hrbank.domain.backup.dto;

import com.sprint.mission.hrbank.domain.backup.entity.BackupStatus;
import java.time.Instant;

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

}
