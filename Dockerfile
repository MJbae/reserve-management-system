FROM openjdk:17

ARG PROJECT="point-management-system"

ENV PROJECT_NAME ${PROJECT}

COPY ${PROJECT_NAME}/build/libs/*.jar ${PROJECT_NAME}.jar

ENTRYPOINT java -jar ${PROJECT_NAME}.jar