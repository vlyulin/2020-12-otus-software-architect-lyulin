# Billing Service

## Собрать приложение

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду: 

```
gradlew :hw10-course-work:BillingService:billing-server:build -x test
или
gradle hw10-course-work:BillingService:billing-server:build -x test
```

## Собрать docker-файла с приложением

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду:  
```
docker build -f ./hw10-course-work/BillingService/Dockerfile.hw10-cource-work.billing-service --build-arg JAR_FILE=./hw10-course-work/BillingService/billing-server/build/libs/*.jar -t vlyulin/billing-service .
```
Или из директории ./hw10-course-work/BillingService
```
docker build -f Dockerfile.hw10-cource-work.billing-service --build-arg JAR_FILE=./billing-server/build/libs/*.jar -t vlyulin/billing-service .
```  
***None***: -t .. сразу установит tag  

## Разместить образ в DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/billing-service
```

## Создать Helm chart для приложения billing-service
mkdir .\hw10-course-work\BillingService\kubernates
cd .\hw10-course-work\BillingService\kubernates
helm create billing-service

### Проверить генерацию templates
```
helm install billing-service .\hw10-course-work\BillingService\kubernates\billing-service --dry-run
```
## Удалить предыдущую установку 
```
helm uninstall billing-service
```

## Установить приложение
```
helm install billing-service .\hw10-course-work\BillingService\kubernates\billing-service
```


# Debug

kubectl exec --stdin --tty billing-service-7d87c6cdf9-9p78p -- /bin/sh

Список контейнеров  
docker ps  
Зайти в контейнер  
docker exec -it e5fc62bc4eee /bin/bash

Проверить работоспособность установленного в kubernates приложения без ingress
kubectl port-forward po/billing-service-7d87c6cdf9-hktvx 8080:8080

Проверить работоспособность сервиса billing-service без ingress
kubectl port-forward svc/billing-service 80:80

Проверить настройки сервиса
kubectl get service billing-service -o json
kubectl describe services billing-service

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
