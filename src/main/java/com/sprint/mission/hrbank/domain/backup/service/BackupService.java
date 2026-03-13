package com.sprint.mission.hrbank.domain.backup.service;

import com.sprint.mission.hrbank.domain.backup.entity.Backup;
import com.sprint.mission.hrbank.domain.backup.entity.BackupStatus;
import com.sprint.mission.hrbank.domain.backup.repository.BackupRepository;
import java.time.Instant;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BackupService {

  private final BackupRepository backupRepository;

  @Transactional
  public Backup createBackup(String workerIp) {
    Optional<Backup> lastCompletedBackup = backupRepository.findFirstByStatusOrderByEndedAtDesc(
        BackupStatus.COMPLETED);

    boolean needsBackup = checkDataChangedSince(lastCompletedBackup);

    if (!needsBackup) {
      Backup skippedBackup = new Backup(workerIp, Instant.now(), BackupStatus.SKIPPED);
      return backupRepository.save(skippedBackup);
    }

    Backup inProgressBackup = new Backup(workerIp, Instant.now(), BackupStatus.IN_PROGRESS);
    return backupRepository.save(inProgressBackup);
  }

  private boolean checkDataChangedSince(Optional<Backup> lastBackup) {
    if (lastBackup.isEmpty() || lastBackup.get().getEndedAt() == null) {
      return true;
    }

    Instant lastBackupTime = lastBackup.get().getEndedAt();
    return true; // 추후 직원 정보 수정 이력 연동 시 변경 예정 (수정 이력을 조회하여 lastBackupTime 이후의 데이터가 있는지 확인 필요)
  }
}
