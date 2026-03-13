package com.sprint.mission.hrbank.domain.backup;

import java.util.List;

public record CursorPageResponseBackupDto(
    List<BackupDto> content,
    String nextCursor,
    Long nextIdAfter,
    Integer size,
    Long totalElements,
    Boolean hasNext
) {

}
