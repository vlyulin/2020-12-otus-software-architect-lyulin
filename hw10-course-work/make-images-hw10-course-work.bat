
docker build -f ./Auth/Dockerfile.hw10-course-work.auth-service --build-arg JAR_FILE=./Auth/build/libs/*.jar -t vlyulin/auth-service .

docker build -f ./BillingService/Dockerfile.hw10-cource-work.billing-service --build-arg JAR_FILE=./BillingService/billing-server/build/libs/*.jar -t vlyulin/billing-service .

docker build -f ./NotificationService/Dockerfile.hw10-cource-work.notification-service --build-arg JAR_FILE=./NotificationService/notification-server/build/libs/*.jar -t vlyulin/notification-service .

docker build -f ./OrderService/Dockerfile.hw10-cource-work.order-service --build-arg JAR_FILE=./OrderService/order-server/build/libs/*.jar -t vlyulin/order-service .

