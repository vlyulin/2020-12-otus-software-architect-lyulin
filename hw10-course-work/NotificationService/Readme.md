# Notification Service

## Собрать приложение

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду: 

```
gradlew :hw10-course-work:NotificationService:notification-server:build -x test
или
gradle hw10-course-work:NotificationService:notification-server:build -x test
```

## Собрать docker-файла с приложением

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду:  
```
docker build -f ./hw10-course-work/NotificationService/Dockerfile.hw10-cource-work.notification-service --build-arg JAR_FILE=./hw10-course-work/NotificationService/notification-server/build/libs/*.jar -t vlyulin/notification-service .
```
Или из директории ./hw10-course-work/NotificationService
```
docker build -f Dockerfile.hw10-cource-work.notification-service --build-arg JAR_FILE=./notification-server/build/libs/*.jar -t vlyulin/notification-service .
```  
***None***: -t .. сразу установит tag  

## Разместить образ в DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/notification-service
```

## Создать Helm chart для приложения Notification-service
mkdir .\hw10-course-work\NotificationService\kubernates
cd .\hw10-course-work\NotificationService\kubernates
helm create notification-service

### Проверить генерацию templates
```
helm install Notification-service .\hw10-course-work\NotificationService\kubernates\notification-server --dry-run
```
## Удалить предыдущую установку 
```
helm uninstall notification-service
```

## Установить приложение
```
helm install notification-service .\hw10-course-work\NotificationService\kubernates\notification-service
```


# Debug

kubectl exec --stdin --tty notification-service-7d87c6cdf9-9p78p -- /bin/sh

Список контейнеров  
docker ps  
Зайти в контейнер  
docker exec -it e5fc62bc4eee /bin/bash

Проверить работоспособность установленного в kubernates приложения без ingress
kubectl port-forward po/notification-server-7d87c6cdf9-hktvx 8080:8080

Проверить работоспособность сервиса notification-service без ingress
kubectl port-forward svc/notification-server 80:80

Проверить настройки сервиса
kubectl get service notification-service -o json
kubectl describe services notification-service

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
