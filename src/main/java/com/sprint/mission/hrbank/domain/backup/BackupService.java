package com.sprint.mission.hrbank.domain.backup;

import com.sprint.mission.hrbank.domain.backup.dto.BackupDto;
import com.sprint.mission.hrbank.domain.backup.dto.BackupSearchRequest;
import com.sprint.mission.hrbank.domain.backup.dto.CursorPageResponseBackupDto;
import com.sprint.mission.hrbank.domain.changelog.repository.ChangeLogRepository;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BackupService {

  private final BackupRepository backupRepository;
  private final ChangeLogRepository changeLogRepository;
  private final BackupMapper backupMapper;

  public CursorPageResponseBackupDto getBackups(BackupSearchRequest request) {
    // TODO: StartedAtFrom startedAtTo 검증 로직 추가 필요
    // 요청 파라미터 정규화
    int size = normalizeSize(request.size());
    String sortField = normalizeSortField(request.sortField());
    Direction sortDirection = normalizeSortDirection(request.sortDirection());
    String worker = normalizeWorker(request.worker());

    // 정렬 조건 생성
    // 1차 정렬 : 사용자가 선택한 필드
    // 2차 정렬 : id
    Sort sort = Sort.by(sortDirection, sortField).and(Sort.by(sortDirection, "id"));

    // Pageable 생성
    Pageable pageable = PageRequest.of(0, size, sort);

    // JPQL 조회
    Slice<Backup> backupSlices = backupRepository.searchBackups(
        worker,
        request.status(),
        request.startedAtFrom(),
        request.startedAtTo(),
        request.idAfter(),
        pageable
    );

    List<BackupDto> content = backupSlices.getContent().stream()
        .map(backupMapper::toDto)
        .toList();

    // totalElements 계산
    long totalElements = backupRepository.countByConditions(
        worker,
        request.status(),
        request.startedAtFrom(),
        request.startedAtTo()
    );

    // 다음 페이지용 id 커서 계산
    Long nextIdAfter = (backupSlices.hasNext() && !content.isEmpty())
        ? content.get(content.size() - 1).id()
        : null;

    // 다음 페이지용 value 커서 계산
    String nextCursor = nextIdAfter != null
        ? buildNextCursor(sortField,
        backupSlices.getContent().get(backupSlices.getNumberOfElements() - 1),
        nextIdAfter)
        : null;

    return new CursorPageResponseBackupDto(
        content,
        nextCursor,
        nextIdAfter,
        pageable.getPageSize(),
        totalElements,
        backupSlices.hasNext()
    );
  }

  @Transactional
  public Backup createBackup(String workerIp) {
    // 가장 최근에 성공한 백업 이력 조회
    Optional<Backup> lastCompletedBackup = backupRepository.findFirstByStatusOrderByEndedAtDesc(
        BackupStatus.COMPLETED);

    // 최근 백업 이후 데이터 변경이 있었는지 확인
    boolean needsBackup = checkDataChangedSince(lastCompletedBackup);

    // 변경 사항 없으면 SKIPPED 상태 저장
    if (!needsBackup) {
      Backup skippedBackup = new Backup(workerIp, Instant.now(), BackupStatus.SKIPPED);
      return backupRepository.save(skippedBackup);
    }

    // 변경 사항 있으면 IN_PROGRESS 상태 저장
    Backup inProgressBackup = new Backup(workerIp, Instant.now(), BackupStatus.IN_PROGRESS);
    return backupRepository.save(inProgressBackup);
  }

  public Optional<BackupDto> getLatestBackup(BackupStatus status) {
    BackupStatus searchStatus = (status == null) ? BackupStatus.COMPLETED : status;
    return backupRepository.findFirstByStatusOrderByEndedAtDesc(searchStatus)
        .map(backupMapper::toDto);
  }

  // ----- 헬퍼 메서드 -----
  // 백업 시점 이후에 데이터 변경이 있었는지 판단
  private boolean checkDataChangedSince(Optional<Backup> lastBackup) {
    if (lastBackup.isEmpty() || lastBackup.get().getEndedAt() == null) {
      return true;
    }

    Instant lastBackupTime = lastBackup.get().getEndedAt();
    Instant now = Instant.now();

    // countChangeLogs를 활용하여 변경 이력 존재 여부 확인
    long changedCount = changeLogRepository.countChangeLogs(lastBackupTime, now);
    return changedCount > 0;
  }

  // size 정규화
  private int normalizeSize(Integer size) {
    if (size == null) {
      return 10; // 기본값 10
    }
    if (size < 1) {
      throw new IllegalArgumentException("size는 1 이상이어야 합니다");
    }
    return size;
  }

  // sortField 정규화
  private String normalizeSortField(String sortField) {
    if (!StringUtils.hasText(sortField)) {
      return "startedAt"; // 기본값 'startedAt'
    }
    if (!"startedAt".equals(sortField)
        && !"endedAt".equals(sortField)
        && !"status".equals(sortField)
    ) {
      throw new IllegalArgumentException("sortField는 startedAt, endedAt, status만 허용됩니다");
    }
    return sortField;
  }

  // normalizeSortDirection 정규화
  private Sort.Direction normalizeSortDirection(String sortDirection) {
    if (!StringUtils.hasText(sortDirection)) {
      return Sort.Direction.DESC;
    }
    if ("asc".equalsIgnoreCase(sortDirection)) {
      return Sort.Direction.ASC;
    }
    if ("desc".equalsIgnoreCase(sortDirection)) {
      return Sort.Direction.DESC;
    }
    throw new IllegalArgumentException("sortDirection은 ASC 또는 DESC만 허용됩니다");
  }

  // worker 정규화
  private String normalizeWorker(String worker) {
    return StringUtils.hasText(worker) ? worker : null;
  }

  // nextCursor 생성
  private String buildNextCursor(String sortField, Backup backup, Long fallbackId) {
    switch (sortField) {
      case "endedAt": // endedAt 기준 정렬
        if (backup.getEndedAt() != null) {
          return backup.getEndedAt().toString();
        } else { // endedAt 값 없을 경우
          return String.valueOf(fallbackId);
        }
      case "status": // status 기준 정렬
        return backup.getStatus().name();
      default: // startedAt 기준 정렬
        return backup.getStartedAt().toString(); // startedAt은 null 값 안 들어옴
    }
  }
}
