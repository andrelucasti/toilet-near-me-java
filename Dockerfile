FROM gradle:7.6.0-jdk17 as builder
COPY ./ ./
RUN gradle clean bootJar

FROM eclipse-temurin:17

WORKDIR /usr/src/app

COPY --from=builder /home/gradle/build/libs/*.jar application.jar

EXPOSE 8181
EXPOSE 8010

ENTRYPOINT java -jar "application.jar" \
            -Dspring.profiles.active=$SPRING_PROFILES_ACTIVE \