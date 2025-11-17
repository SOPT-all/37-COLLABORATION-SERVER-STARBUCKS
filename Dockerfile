# 1. Java 17 Runtime만 포함된 경량 버전
FROM eclipse-temurin:17-jdk-jammy

# 2. 작업 디렉토리 설정
WORKDIR /app

# 3. 빌드된 JAR 파일 위치 변수 설정
ARG JAR_FILE=build/libs/*.jar

# 4. 호스트의 JAR 파일을 컨테이너 내부로 복사
COPY ${JAR_FILE} app.jar

# 5. 타임존 설정
ENV TZ=Asia/Seoul

# 6. 실행 명령어
ENTRYPOINT ["java", "-jar", "/app/app.jar"]