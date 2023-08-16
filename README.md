
# wanted-pre-onboarding-backend
wanted-pre-onboarding-backend

## 지원자명
강수나

## 애플리케이션 실행 방법

1. 사용한 툴
   - Java 17
   - SpringBoot
   - MySQL 8.0
   - JPA
2. BackApplication 실행 후 엔드포인트 api 호출

엔드포인트 호출 방법은 아래와 같습니다.

---

**과제 1. 사용자 회원가입 엔드포인트**

- URL: localhost:8080/user/join (POST)

```
{
    "email" : "aaaa@gmail.com",
    "password" : "test!!!123123123"
}
```

**과제 2. 사용자 로그인 엔드포인트**

- URL: localhost:8080/user/login (POST)

```
{
    "email" : "aaaa@gmail.com",
    "password" : "test!!!123123123"
}
```


**과제 3. 새로운 게시글을 생성하는 엔드포인트**

- URL: localhost:8080/board (POST)
- 헤더의 Authorization 사용
```
{
    "title" : "제목입니다~",
    "contents" : "내용입니다~"
}
```

**과제 4. 게시글 목록을 조회하는 엔드포인트**

- URL: localhost:8080/board?page=0 (GET)

**과제 5. 특정 게시글을 조회하는 엔드포인트**

- URL: localhost:8080/board/1 (GET)

**과제 6. 특정 게시글을 수정하는 엔드포인트**

- URL: localhost:8080/board (PUT)
- 헤더의 Authorization 사용
```
{
    "id" : 1,
    "title" : "수정된 제목입니다~",
    "contents" : "수정된 내용입니다~",
    "author" : "aaaa@gmail.com"
}
```

**과제 7. 특정 게시글을 삭제하는 엔드포인트**

- URL: localhost:8080/board/1 (DELETE)
- 헤더의 Authorization 사용

## 데이터베이스 테이블 구조
![img.png](img.png)

## 데모 영상 링크
**과제 1. 사용자 회원가입 엔드포인트**


https://github.com/acrnm148/wanted-pre-onboarding-backend/assets/67724306/1a1b7cc8-7193-44f1-8031-1d1d2483dc25

- 클라이언트로부터 JoinRequestDto를 받고 이메일, 비밀번호 유효성 검사 진행
- 유효성 검사 통과 시 user 테이블에 저장


**과제 2. 사용자 로그인 엔드포인트**


https://github.com/acrnm148/wanted-pre-onboarding-backend/assets/67724306/7f72e6f3-f17e-43c8-9f2b-7d0a062e97a0

- 클라이언트로부터 LoginRequestDto를 받고 이메일, 비밀번호 유효성 검사 진행
- 유효성 검사 통과 이후 이메일로 유저 조회 시 존재할 경우 로그인 진행
- accessToken, refreshToken 생성, refreshToken은 DB에 저장
   - accessToken, refreshToken의 claim에는 유저id, email을 비밀키를 사용하여 base64로 암호화한 값이 들어간다.


**과제 3. 새로운 게시글을 생성하는 엔드포인트**


https://github.com/acrnm148/wanted-pre-onboarding-backend/assets/67724306/8501e76a-af90-44cd-9b69-7f8de62e3407

- requestHeader로부터 Authorization 이름의 token값과 requestBody로부터 BoardRequestDto값을 파라미터로 받는다.
- 유효성 검사 진행 : token이 null이거나 길이가 7보다 작을 경우("Bearer "보다 길이가 짧을 경우), BoardRequestDto값이 없을 경우 불가
- 유효성 검사 통과 시 BoardRequestDto를 토대로 게시글 생성, 작성자는 token으로부터 email을 디코딩하여 저장


**과제 4. 게시글 목록을 조회하는 엔드포인트**


https://github.com/acrnm148/wanted-pre-onboarding-backend/assets/67724306/e3469bbb-70ff-4f16-a755-e00c7ab62c53

- RequestParam으로 page값을 받아온다.
- 전체 게시글 중 page별로 최대 10개 게시글 기준으로 목록을 반환한다.(최대 개수는 변경 가능)


**과제 5. 특정 게시글을 조회하는 엔드포인트**


https://github.com/acrnm148/wanted-pre-onboarding-backend/assets/67724306/71a07b2a-ada4-436e-b778-46b004fbaa9d

- BoardId를 PathVariable로 받아서 해당 게시글을 조회한다.
- 게시글이 없을 경우 error log가 발생한다.


**과제 6. 특정 게시글을 수정하는 엔드포인트**


https://github.com/acrnm148/wanted-pre-onboarding-backend/assets/67724306/86b814e3-da1f-457a-96ca-26488ad0431c

- RequestHeader로 Authorization이름의 token을 받고, RequestBody로 BoardRequestDto값을 받는다.
- token이 null이 아니고 길이가 7 이상이며, BoardRequestDto가 null이 아니어야 한다.
- token을 디코딩하여 얻은 email값이 게시글의 작성자(author)와 일치할 경우 BoardRequestDto를 토대로 게시글 수정이 완료되며, 불일치할 경우 수정에 실패한다.


**과제 7. 특정 게시글을 삭제하는 엔드포인트**


https://github.com/acrnm148/wanted-pre-onboarding-backend/assets/67724306/4d249427-8aa8-41fe-a54b-f6c398ac1e0a

- RequestHeader로 Authorization이름의 token을 받고, PathVariable로 boardId 값을 받는다.
- token이 null이 아니고 길이가 7이상이어야 한다.
- token을 디코딩하여 얻은 email값이 게시글의 작성자(author)와 일치할 경우 boardId에 해당하는 게시글이 삭제되며, 불일치할 경우 삭제에 실패한다.


## API 명세

![image](https://github.com/acrnm148/wanted-pre-onboarding-backend/assets/67724306/32653b88-ae52-410f-b7f4-453fe299e323)



[//]: # (### docker-compose 실행 방법)

[//]: # (### 배포된 API 주소)

[//]: # (### 설계한 AWS 환경)


