call gradle :hw10-course-work:OrderService:order-server:clean 
call gradle :hw10-course-work:OrderService:order-server:build -x test
rem -Dspring.profiles.active=kubernates

docker build -f ./Dockerfile.hw10-cource-work.order-service --build-arg JAR_FILE=./order-server/build/libs/*.jar -t vlyulin/order-service .

helm uninstall order-service
helm install order-service .\kubernates\order-service

