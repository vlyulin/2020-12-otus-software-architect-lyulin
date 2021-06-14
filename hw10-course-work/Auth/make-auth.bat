call gradle clean
call gradle build -x test

docker build -f Dockerfile.hw10-course-work.auth-service --build-arg JAR_FILE=./build/libs/*.jar -t vlyulin/auth-service .

helm uninstall auth-service
helm install auth-service .\kubernates\auth-service

