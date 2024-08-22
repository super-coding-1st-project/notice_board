# Notice_Board BE Project
2406 1차 프로젝트

# 게시판 만들기 프로젝트
- 현재 구현된 기능
  - 회원가입, 로그인, 로그아웃 구현(Spring security, JWT 토큰 사용하여 구현)
  - 게시물 작성, 수정, 삭제, 조회 기능 구현 (Spring JPA, MySQL 사용하여 구현)
  - 댓글 작성, 수정, 기능 구현 (Spring JPA, MySQL 사용하여 구현)
  - 좋아요, 좋아요 취소 기능 구현 (Spring JPA, MySQL 사용하여 구현)

# 실행 전 설정해야할 내용

1. DB 세팅
  - table_ddl.sql 파일을 사용하여 DB에 데이터베이스와 테이블을 추가

2. application.yml 파일 설정
  - datasource.url : 로컬에 맞게 DB주소 수정 필요

3. 환경변수 세팅 (각 환경변수를 생성하여 value 설정하고 로컬 환경에 맞게 세팅 변경해야함)
  - DATABASE_USERNAME : DB에 접근하기 위한 아이디 (1에서 설정한 DB에 접속하기 위한 ID)
  - DATABASE_PASSWORD : DB에 접근하기 위한 비밀번호 (1에서 설정한 DB에 접속하기 위한 PW)
  - JWT_SECRET_KEY : 사용자에 맞게 설정.
  - TOKEN_VALIDE_TIME : 기본값 3600000ms(한시간으로 설정)




