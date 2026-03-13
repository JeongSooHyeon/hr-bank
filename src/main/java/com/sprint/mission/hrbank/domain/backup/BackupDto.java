package com.sprint.mission.hrbank.domain.backup;

import java.time.Instant;

public record BackupDto(
    Long id,
    String worker,
    Instant startedAt,
    Instant endedAt,
    String status,
    Long fileId
) {

}
