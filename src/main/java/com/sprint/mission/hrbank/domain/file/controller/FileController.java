package com.sprint.mission.hrbank.domain.file.controller;

import com.sprint.mission.hrbank.domain.baseentity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "files")
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
public class FileController extends BaseEntity {

  // id

  @Column(nullable = false)
  private String originalName;

  @Column(nullable = false)
  private String storedName;

  @Column(nullable = false)
  private String contentType;

  @Column(nullable = false)
  private Long size;

  @Column(nullable = false)
  private String storagePath;

  // createdAt
}
