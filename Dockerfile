FROM eclipse-temurin:21 AS builder

COPY *.gradle gradle.* gradlew /src/
COPY gradle /src/gradle
WORKDIR /src
RUN ./gradlew --version

COPY . .

RUN ./gradlew build

FROM eclipse-temurin:21

ARG VERSION
COPY --from=builder /src/build/libs/mailing-service-${VERSION}.jar /mailing-service.jar

HEALTHCHECK --interval=30s --timeout=10s --retries=3 CMD curl -f http://localhost:8080/actuator/health || exit 1

ENTRYPOINT ["java","-jar","/mailing-service.jar"]