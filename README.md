# point-management-system

### Table of Contents
[I. 실행 환경 구축 방법](#i-실행-환경-구축-방법)

[II. HTTP API 요청 방법](#ii-http-api-요청-방법)

[III. 프로젝트 구조](#iii-프로젝트-구조)

[IV. TDD 기반 구현 절차](#iv-tdd-기반-구현-절차)

[V. 테스트 구조](#v-테스트-구조)

[VI. 기능별 순서도](#vi-기능별-순서도)


### I. 실행 환경 구축 방법
프로젝트 루트에서 아래 명령을 실행해주세요.
```shell
chmod +x build_and_run.sh && ./build_and_run.sh
```
- 위 스크립트는 로컬 머신 프로세서에 따라 docker-compose 파일을 선택하여 실행 환경을 구축합니다.
- docker 기반 실행환경의 서버 포트는 **8080**입니다. 다만 IDE로 서버를 직접 실행하신다면 포트는 **55123**입니다. 
- DB 포트는 **3311**입니다. ID와 PW는 각각 **root**, **password**입니다.

### II. HTTP API 요청 방법
- docker 기반 실행환경의 **host**는 **http://localhost:8080**입니다. 다만 IDE로 서버를 직접 실행하신다면 포트는 **http://localhost:55123**입니다.
- HTTP API 요청의 공통 path param인 **memberId**는 '**128c4d0f68a34e1ca3c8b51cc01a4b71**'를 사용하실 수 있습니다.
   ```
   ### 회원별 적립금 합계 조회 API
   GET {{host}}/api/members/{{memberId}}/points/total
   
   ### 회원별 적립금 적립/사용 내역 조회 API
   GET {{host}}/api/members/{{memberId}}/points
   
   ### 회원별 적립금 적립 API
   POST {{host}}/api/members/{{memberId}}/points
   Content-Type: application/json
   
   {
       "points": 20
   }
   
   ### 회원별 적립금 사용 API
   PUT {{host}}/api/members/{{memberId}}/points/use
   Content-Type: application/json
   
   {
       "points": 10
   }
   
   
   ### 회원별 적립금 사용취소 API
   PUT {{host}}/api/members/{{memberId}}/points/cancel
   ```


### III. 프로젝트 구조
본 프로젝트는 4개의 주요 패키지로 구성되어 있습니다.
- **domain**: 핵심 비지니스 로직과 상태를 모델링합니다.
- **usecase**: 도메인 모델을 조합하여 비지니스적으로 의미있는 단위의 기능을 구현합니다.
- **infra**: 도메인과 결합할 필요가 없는 내부 시스템의 기능을 구현합니다.
- **controller**: 외부 시스템과의 통신을 위한 인터페이스를 정의합니다.


### IV. TDD 기반 구현 절차
1. 기능을 직관적으로 이해하기 위해 아래와 같이 **기능별 순서도**를 작성합니다.
2. 순서도의 Happy Path를 간략하게 구현합니다.
3. 순서도의 주요 분기에 대해 실패하는 테스트를 작성합니다.
4. 실패하는 테스트를 통과하도록 코드를 수정합니다.
5. 3단계, 4단계 반복하며 기능의 완성도를 높입니다.


### V. 테스트 구조
고전파 스타일로 테스트를 구성했습니다. 테스트에 대한 철학은 ['단위 테스트 by 블라디미르 코리코프'](https://studynote.oopy.io/books/15)의 영향을 받았습니다.
- e2e 테스트: IntelliJ IDE에서 지원하는 .http을 활용하여 5개의 API를 실제 호출하고 정상 동작여부를 검증합니다.
- 통합 테스트: Kotest 기반으로 5개 API의 13개 핵심 동작에 대해 검증합니다.
- 단위 테스트: 핵심 도메인 모델인 PointAccount의 주요 비지니스 로직을 검증합니다.


### VI. 기능별 순서도
1. 회원별 적립금 합계 조회

    ```mermaid
    graph TB
    시작(시작) --> 회원존재여부[회원 존재 ?]
    회원존재여부 -->|예| 적립금보기[적립금 합계 응답]
    회원존재여부 -->|아니오| 회원미존재예외[회원 미존재 응답]
    ```

2. 회원별 적립금 적립

    ```mermaid
    graph TB
    시작(시작) --> 회원존재여부[회원 존재 ?]
    회원존재여부 -->|예| 적립금확인[적립 대상 금액 유효 ?]
    회원존재여부 -->|아니오| 회원미존재예외[회원 미존재 응답]
    적립금확인 -->|예| 적립금추가[적립금 추가]
    적립금확인 -->|아니오| 유효하지않은금액[대상 금액 무효 응답]
    적립금추가 --> 성공메세지[적립 성공 응답]
    ```

3. 회원별 적립금 적립/사용 내역 조회

    ```mermaid
    graph TB
    시작(시작) --> 회원존재여부[회원 존재 ?]
    회원존재여부 -->|예| 거래내역확인[회원 적립금 내역 존재 ?]
    회원존재여부 -->|아니오| 회원미존재예외[회원 미존재 응답]
    거래내역확인 -->|예| 거래목록보기[적립/사용 내역 응답]
    거래내역확인 -->|아니오| 거래내역없음[빈 내역 응답]
    ```

4. 회원별 적립금 사용

    ```mermaid
    graph TB
    시작(시작) --> 회원존재여부[회원 존재 ?]
    회원존재여부 -->|예| 적립금충분확인[적립금 사용 가능 ?]
    회원존재여부 -->|아니오| 회원미존재예외[회원 미존재 응답]
    적립금충분확인 -->|예| 적립금사용[적립금 사용]
    적립금충분확인 -->|아니오| 적립금부족[적립금 부족 응답]
    적립금사용 --> 적립금차감응답[적립금 사용 성공 응답]
    ```

5. 회원별 적립금 사용취소

    ```mermaid
    graph TB
    시작(시작) --> 회원존재여부[회원 존재 ?]
    회원존재여부 -->|예| 거래존재확인[거래 존재 ?]
    회원존재여부 -->|아니오| 회원미존재예외[회원 미존재 응답]
    거래존재확인 -->|예| 거래취소가능여부[적립금 사용취소 가능 ?]
    거래존재확인 -->|아니오| 거래존재안함[거래 미존재 응답]
    거래취소가능여부 -->|예| 거래취소가능[사용 취소]
    거래취소가능여부 -->|아니오| 거래취소불가[취소 불가 응답]
    거래취소가능 --> 거래취소성공응답[취소 성공 응답]
    ```
