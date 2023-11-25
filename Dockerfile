FROM maven:3.9.4 AS build

LABEL authors="farneser"

WORKDIR /app

COPY pom.xml /app

RUN mvn dependency:resolve

COPY src /app/src

COPY checkstyle.xml /app/checkstyle.xml

ENV POSTGRES_HOST ${POSTGRES_HOST}
ENV POSTGRES_PORT ${POSTGRES_PORT}
ENV POSTGRES_DB ${POSTGRES_DB}
ENV POSTGRES_USERNAME ${POSTGRES_USERNAME}
ENV POSTGRES_PASSWORD ${POSTGRES_PASSWORD}
ENV JWT_SECRET ${JWT_SECRET}
ENV USE_SWAGGER ${USE_SWAGGER}
ENV SERVER_PORT ${SERVER_PORT}
ENV LOGGING_LEVEL ${LOGGING_LEVEL}
ENV ALLOWED_ORIGINS ${ALLOWED_ORIGINS}
ENV RABBITMQ_HOST ${RABBITMQ_HOST}
ENV RABBITMQ_PORT ${RABBITMQ_PORT}
ENV RABBITMQ_USERNAME ${RABBITMQ_USERNAME}
ENV RABBITMQ_PASSWORD ${RABBITMQ_PASSWORD}

RUN mvn -f /app/pom.xml clean package -Dcheckstyle.skip=true -DskipTests

FROM openjdk:17-jdk-slim as production

COPY --from=build /app/target/task-tracker-api*.jar /usr/local/lib/task-tracker-api.jar

EXPOSE ${SERVER_PORT}

ENTRYPOINT ["java","-jar","/usr/local/lib/task-tracker-api.jar"]