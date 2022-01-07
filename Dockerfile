FROM openjdk:17-jdk-alpine
ARG JAR_FILE=target/*.jar
# Switch to working directory
RUN mkdir -p /usr/local/cabinet
WORKDIR /usr/local/cabinet
COPY cabinet.db cabinet.db
# Copy JAR file to working directory
COPY ${JAR_FILE} cabinet.jar
# ENTRYPOINT [ "ls" ]
# ENTRYPOINT [ "ls", "-l", "/usr/local/cabinet/" ] 
ENTRYPOINT ["java","-jar","cabinet.jar"]
# ENTRYPOINT [ "sh" ]