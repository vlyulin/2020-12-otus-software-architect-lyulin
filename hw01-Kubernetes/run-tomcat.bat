REM The --rm causes Docker to automatically remove the container when it exits.
docker container run --rm -d -p 8080:8080 tomcat:8.0.51-jre8-alpine
