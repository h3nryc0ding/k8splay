FROM eclipse-temurin:21.0.6_7-jdk-alpine-3.21@sha256:cafcfad1d9d3b6e7dd983fa367f085ca1c846ce792da59bcb420ac4424296d56 AS build

COPY gradle gradle
COPY gradlew build.gradle.kts settings.gradle.kts ./
RUN ./gradlew dependencies --no-daemon
COPY src src
RUN ./gradlew bootJar --no-daemon

FROM eclipse-temurin:21.0.6_7-jre-alpine-3.21@sha256:4e9ab608d97796571b1d5bbcd1c9f430a89a5f03fe5aa6c093888ceb6756c502

COPY --from=build /build/libs/*.jar livechat.jar

EXPOSE 8080
HEALTHCHECK --interval=30s --timeout=3s --retries=3 \
  CMD wget -q --spider http://localhost:8081/actuator/health || exit 1
ENTRYPOINT [ "java", "-jar", "livechat.jar" ]
