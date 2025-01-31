# 도커 이미지를 컨테이너로 실행할 경우 jre사용
FROM openjdk:17-alpine
# target안에 패키징된 jar파일을 app.jar라는 이미지 파일로 복사
COPY build/libs/*.jar app.jar
# 도커가 해당 이미지를 실행할때 사용하는 명령어를 명시 ex. java -jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]


