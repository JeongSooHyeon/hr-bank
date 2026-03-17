package com.sprint.mission.hrbank.domain.backup;

import java.time.Instant;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface BackupRepository extends JpaRepository<Backup, Long>,
    JpaSpecificationExecutor<Backup> {

  Optional<Backup> findFirstByStatusOrderByEndedAtDesc(BackupStatus status);

  Page<Backup> findAllByOrderByStartedAtDesc(Pageable pageable); // 전체 백업 이력 최신순 조회 (추후 페이징에 활용할 예정)

  Page<Backup> findAllByStatusOrderByStartedAtDesc(
      BackupStatus status,
      Pageable pageable
  ); // 상태별 백업 이력 필터링 조회 (추후 페이징에 활용할 예정)

  Slice<Backup> searchBackups(String worker, BackupStatus status, Instant instant, Instant instant1,
      Long aLong, Pageable pageable);

  long countByConditions(String worker, BackupStatus status, Instant instant, Instant instant1);
}
