docker build -f Dockerfile.hw10-cource-work.order-service --build-arg JAR_FILE=./order-server/build/libs/*.jar -t vlyulin/order-service .
cd ./kubernates
helm uninstall order-service
helm install order-service ./order-service
cd ..