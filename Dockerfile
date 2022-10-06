FROM eclipse-temurin:17-alpine

WORKDIR /app

ARG JAR_FILE

COPY target/${JAR_FILE} /app/api.jar
COPY wait-for-it.sh /wait-for-it.sh

RUN chmod +x /wait-for-it.sh
RUN apk update && apk add bash

EXPOSE 8080

CMD ["java", "-jar", "api.jar"]