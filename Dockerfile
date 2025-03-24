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
ENTRYPOINT ["java","-jar","/mailing-service.jar"]