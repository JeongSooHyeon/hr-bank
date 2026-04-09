# HR Bank — 직원 정보 관리 시스템

> 기업의 직원 정보를 관리하는 Open EMS (Employee Management System)

[![Java](https://img.shields.io/badge/Java-17-orange)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen)](https://spring.io/projects/spring-boot)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)](https://www.postgresql.org)

---

## 📌 프로젝트 소개

6인 팀 프로젝트로, 도메인 단위로 역할을 분담하여 개발했습니다.  
본인 담당: **직원 수정 이력 관리 도메인 전체**

### 주요 기능
- 직원 등록 / 수정 / 삭제
- 부서 관리
- 파일 관리
- **직원 수정 이력 관리** ← 본인 담당
- 데이터 백업
- 대시보드

---

## 🙋 본인 담당 역할

- `CHANGE_LOGS`, `CHANGE_LOG_DIFFS` 테이블 설계 및 JPA 엔티티 구현
- 직원 추가/수정/삭제 시 이력 자동 저장 서비스 로직 구현
- 수정된 속성만 필터링하여 변경 전/후 값을 저장하는 diff 로직 구현
- 커서 기반 페이지네이션 + 동적 검색 조건을 지원하는 목록 조회 API 구현
- 이력 상세 조회 및 건수 조회 API 구현
- MapStruct 기반 Mapper 구현
- Swagger API 문서화
- 클라이언트 IP 주소 추출 유틸 구현
- 네이밍 컨벤션 · Git 브랜치 전략 Notion 문서화 주도

---

## 🛠 기술 스택

| 분류 | 기술 |
|------|------|
| Language | Java 17 |
| Framework | Spring Boot, Spring Data JPA |
| Database | PostgreSQL |
| Mapping | MapStruct |
| API 문서 | Swagger |
| 협업 | Git / GitHub, Notion |

---

## 💡 핵심 기술 의사결정

### 1. Snapshot 방식 도입

**문제 상황**  
이력 상세 조회 시 `Employee` 엔티티에서 `employeeName`, `profileImageId`를 실시간으로 참조하는 방식으로 처음 구현했습니다. 그런데 직원이 삭제된 경우 해당 엔티티를 찾지 못해 오류가 발생하는 문제가 있었습니다.

**해결**  
`ChangeLog` 엔티티에 `employeeNameSnapshot`, `profileImageIdSnapshot` 필드를 추가해 이력 저장 시점의 데이터를 함께 저장하는 **Snapshot 방식**으로 변경했습니다.

**결과**  
직원 삭제 후에도 이력 상세 조회가 정상적으로 동작합니다.

---

### 2. 커서 기반 페이지네이션 적용

**이유**  
offset 방식은 페이지가 깊어질수록 앞 데이터를 전부 스캔해야 해 데이터가 많아질수록 성능이 저하됩니다. ID 기반 커서 방식은 마지막으로 조회한 ID 이후부터 읽기 때문에 데이터 양과 무관하게 일정한 성능을 유지합니다.

**구현**  
`Slice`를 활용해 전체 카운트 쿼리 없이 `hasNext`를 처리하고, `totalElements`는 별도 count 쿼리로 분리했습니다.

---

### 3. PostgreSQL CAST로 null 파라미터 타입 오류 해결

**문제 상황**  
`Instant` 타입 파라미터가 `null`로 넘어올 때 PostgreSQL이 타입을 추론하지 못해  
`could not determine data type of parameter $11` 오류가 발생했습니다.

**해결**  
JPQL 쿼리에서 `CAST(:atFrom AS timestamp)`로 타입을 명시적으로 지정해 해결했습니다.

---

## 📁 담당 도메인 패키지 구조

```
src/main/java/
└── domain/
    └── changelog/
        ├── controller/   # ChangeLogController
        ├── service/      # ChangeLogService
        ├── repository/   # ChangeLogRepository
        ├── entity/       # ChangeLog, ChangeLogDiff
        ├── dto/          # Request / Response DTO
        ├── mapper/       # MapStruct Mapper
        └── util/         # IpAddressExtractor
```

---

## 🔧 향후 개선 사항

- **복합 커서 도입**: 현재 ID 단일 커서 방식은 `ipAddress` 정렬 시 동일 값이 여러 개일 경우 페이지네이션이 불안정할 수 있음. 정렬 필드 + ID를 조합한 복합 커서로 개선 예정
- **QueryDSL 전환**: 현재 JPQL로 구현된 동적 쿼리를 QueryDSL로 전환하면 컴파일 타임 오류 감지 및 가독성 향상 가능

---

## 👥 팀 구성

| 역할 | 인원 |
|------|------|
| 백엔드 개발 | 6명 |
| 본인 기여도 | 약 20% (이력 관리 도메인 단독 담당) |

> 팀 레포지토리: [github.com/sb10-part2-team2](https://github.com/sb10-part2-team2/sb10-part2-team2)
