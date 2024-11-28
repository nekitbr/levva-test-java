FROM amazoncorretto:21-alpine

WORKDIR "/levva-test"

COPY "./" "./"

RUN chmod +x gradlew
RUN ./gradlew build -x test --no-daemon

EXPOSE 8080

CMD ["java", "-jar", "./build/libs/levva-test-0.1.jar"]
