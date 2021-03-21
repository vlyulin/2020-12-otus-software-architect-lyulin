# Шаги:

## Собрать приложение

Из директории проекта 2020-12-otus-software-architect-lyulin
выполнить команду: 

```
gradlew :hw05-Apigateway:Auth:build
или
gradle :hw05-Apigateway:Auth:build
```

Проверить запуск приложения
```
java -jar hw05-Apigateway/App/build/libs/App.jar
```

## Собрать docker-файла с приложением

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду:  
```
docker build -f ./hw05-Apigateway/Auth/Dockerfile.hw05-apigateway-auth --build-arg JAR_FILE=./hw05-Apigateway/Auth/build/libs/*.jar -t vlyulin/hw05-apigateway-auth .
```  
***None***: -t vlyulin/hw05-apigateway-auth сразу установит tag  

### Проверить запуск приложения из docker
```
docker run -p 8080:8080 -t vlyulin/hw05-apigateway-auth
```
## Разместить образ в DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/hw05-apigateway-auth
```

## Создать Helm chart для приложения hw02-library-app
mkdir .\hw05-Apigateway\Auth\kubernates\
cd .\hw05-Apigateway\Auth\kubernates\
helm create hw05-apigateway-auth

### Проверить генерацию templates
```
helm install hw05-apigateway-auth ./hw05-apigateway-auth --dry-run
```
## Удалить предыдущую установку 
```
helm uninstall hw05-apigateway-auth
```

## Установить приложение
```
helm install --replace hw05-apigateway-auth ./hw05-apigateway-auth
```

## Разное 
### Определение доменного имени сервиса
***На основе***: https://medium.com/kubernetes-tutorials/kubernetes-dns-for-services-and-pods-664804211501
kubectl exec -it hw05-apigateway-auth-6b5f58fb87-jtrct -- sh
PING hw05-apigateway-auth.default.svc.cluster.local (10.97.121.80): 56 data bytes

***Note***
Как выполнять debug контейнера
https://kubernetes.io/docs/tasks/debug-application-cluster/debug-running-pod/
