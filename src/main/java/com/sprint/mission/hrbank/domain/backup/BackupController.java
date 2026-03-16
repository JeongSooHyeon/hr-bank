package com.sprint.mission.hrbank.domain.backup;

import com.sprint.mission.hrbank.domain.backup.dto.BackupDto;
import com.sprint.mission.hrbank.domain.changelog.IpUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/backups")
public class BackupController {

  private final BackupService backupService;
  private final BackupMapper backupMapper;

  @PostMapping
  public ResponseEntity<BackupDto> createBackup(HttpServletRequest request) {
    // 클라이언트의 IP 주소 추출
    String clientIp = IpUtil.getClientIp(request);

    // 백업 필요 여부 판단 및 이력 저장
    Backup backup = backupService.createBackup(clientIp);

    return ResponseEntity.ok(backupMapper.toDto(backup));
  }
}
