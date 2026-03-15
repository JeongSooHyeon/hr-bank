package com.sprint.mission.hrbank.domain.file.controller;

import com.sprint.mission.hrbank.domain.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/files")
@RequiredArgsConstructor
public class FileController {

  private final FileService fileService;

  // 파일 다운로드 - /api/files/{id}/download - 파일을 다운로드합니다.
  // 200 - 다운로드 성공
  // 404 - 파일을 찾을 수 없음
  // 500 - 서버 오류
  @GetMapping("/{id}/download")
  public ResponseEntity<Resource> download(@PathVariable("id") Long id) {
    return null; // TODO: 추후 구현
  }
}
