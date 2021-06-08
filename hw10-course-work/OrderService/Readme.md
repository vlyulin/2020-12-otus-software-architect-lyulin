# Order Service

## Собрать приложение

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду: 

```
gradlew :hw10-course-work:OrderService:order-server:build -x test
или
gradle hw10-course-work:OrderService:order-server:build -x test
```

## Собрать docker-файла с приложением

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду:  
```
docker build -f ./hw10-course-work/OrderService/Dockerfile.hw10-cource-work.order-service --build-arg JAR_FILE=./hw10-course-work/OrderService/order-server/build/libs/*.jar -t vlyulin/order-service .
```
Или из директории ./hw10-course-work/OrderService
```
docker build -f Dockerfile.hw10-cource-work.order-service --build-arg JAR_FILE=./order-server/build/libs/*.jar -t vlyulin/order-service .
```  
***None***: -t .. сразу установит tag  

## Разместить образ в DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/order-service
```

## Создать Helm chart для приложения order-service
mkdir .\hw10-course-work\OrderService\kubernates
cd .\hw10-course-work\OrderService\kubernates
helm create order-service

### Проверить генерацию templates
```
helm install order-service .\hw10-course-work\OrderService\kubernates\order-service --dry-run
```
## Удалить предыдущую установку 
```
helm uninstall order-service
```

## Установить приложение
```
helm install order-service .\hw10-course-work\OrderService\kubernates\order-service
```


# Debug

kubectl exec --stdin --tty order-service-7d87c6cdf9-9p78p -- /bin/sh

Список контейнеров  
docker ps  
Зайти в контейнер  
docker exec -it e5fc62bc4eee /bin/bash

Проверить работоспособность установленного в kubernates приложения без ingress
kubectl port-forward po/order-service-7d87c6cdf9-hktvx 8080:8080

Проверить работоспособность сервиса order-service без ingress
kubectl port-forward svc/order-service 80:80

Проверить настройки сервиса
kubectl get service order-service -o json
kubectl describe services order-service

Если нет конекта от сервиса к pod (приложению) надо проверить настройку в deployment.yaml 
Должно быть не 
ports:
            - name: http
              containerPort: 80
              protocol: TCP

а
ports:
            - name: http
              containerPort: {{ .Values.service.externalPort }}
              protocol: TCP
