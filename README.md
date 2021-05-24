# 2021 풀스택 Uploader

## 0. 빌드 및 실행
~~~
$ cd front-end
$ npm install
$ npm run build

// jar build
$ cd ..
$ cd backend
$ mvnw clean package

// 실행
$ java -jar ./target/uploader-1.0.jar
~~~

## 1. 개발환경
 * Spring-boot-2.4.0
 * h2
 * vuejs 2.6
 * Vuedropzonejs
 * VueBootStrap
 * lombok
 
## 2. 요구 및 제약사항
 * 파일 업로드 (CSV 10만건)
 * 파일 업로드 및 insert 진행상태 progressBar로 표시
 * RestApi 기반
 
## 3.기능
 * 파일 업로드
    * uuid 생성 
    * 실패 및 성공 count정보를 생성
    * 파일 업로드 시 1메가 단위로 파일을 나누어 전송
    * 서버에서 chunk를 조립 파일은 uuid를 파일 명으로 사용
 * 데이터 insert
    * 대량의 데이터 insert를 위해 csv를 라인단위로 읽어 리스트에 저장
    * batchSize 만큼 데이터를 리스트에 담아 batchInsert 실행
    * 실패 및 성공 건수를 계산하여 리턴
    * 사용한 uuid를 제거
 * 성공 및 실패상태 확인
    * 일정 주기로 클라이언트에서 폴링 발생
    * 폴링에 대한 리턴값으로 count정보를 리턴
 
## 4.API
### 4.1 UUID 발급

Request
~~~
request(GET) : http://localhost:9000/api/uuid
~~~

Response
~~~
{"code":"0000","message":"성공","body":"8d9e8b65-e810-46c4-b3e1-87c1f5e0aaef"}
~~~

### 4.2 파일 업로드

Request
~~~
request(GET) : http://localhost:9000/api/upload
[Content-Type:"multipart/form-data;charset=UTF-8", X-UPLOAD-UUID:"e336897f-ae47-4f6d-a805-3f9fa5e22f05"]
~~~

Response
~~~
{"code":"0000","message":"성공","body":{"successCount":101,"failCount":0}}
~~~

### 4.3 카운트 정보 
Request
~~~
request(GET) : http://localhost:9000/api/inquire
[Content-Type:"application/json;charset=UTF-8", X-UPLOAD-UUID:"1c8c832c-04ac-4522-b8be-f916ce9814dd"]~~~

Response
~~~
{"code":"0000","message":"성공","body":{"successCount":101,"failCount":0}}
~~~
