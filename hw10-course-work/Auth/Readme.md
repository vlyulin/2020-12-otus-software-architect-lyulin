# Шаги:

## Отладка
Для запуска Auth сервиса на другом порту надо указать требуемый порт в VM Options в профиле запуска.  
Например:  
```
-Dserver.port=8085
```
![Server.port](./imgs/VMOptions.png)

Для соединения с базой данной в режиме отладки надо установить переменные окружения в поле "Environment Variables":  
```
DB_DRIVER=org.postgresql.Driver;DB_URL=jdbc:postgresql://localhost:5432/postgres;DB_USERNAME=postgres;DB_PASSWORD=pswd
```

![Environment Variables](./imgs/EnvironmentVariables.png)

## Собрать приложение
Из директории проекта 2020-12-otus-software-architect-lyulin
выполнить команду: 

```
gradlew :hw10-course-work:Auth:build
или
gradle :hw10-course-work:Auth:build
```

Проверить запуск приложения
```
java -DDB_DRIVER=org.postgresql.Driver -DDB_URL=jdbc:postgresql://localhost:5432/postgres -DDB_USERNAME=postgresadmin -DDB_PASSWORD=pswd -jar ./hw10-course-work/Auth/build/libs/Auth-1.0.0.jar
```

## Собрать docker-файла с приложением

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду:  
```
docker build -f ./hw10-course-work/Auth/Dockerfile.hw10-course-work.auth-service --build-arg JAR_FILE=./hw10-course-work/Auth/build/libs/*.jar -t vlyulin/auth-service .
```  
или 
```
docker build -f ./Dockerfile.hw10-course-work.auth-service --build-arg JAR_FILE=./build/libs/*.jar -t vlyulin/auth-service .
```  
***None***: -t vlyulin/auth-service сразу установит tag  

### Проверить запуск приложения из docker
```
docker run -p 8080:8080 -t vlyulin/auth-service
```
## Разместить образ в DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/auth-service
```

## Создать Helm chart для приложения hw02-library-app
mkdir .\hw10-course-work\Auth\kubernates\
cd .\hw10-course-work\Auth\kubernates\
helm create auth-service

### Внести изменения в helm templates
- .\hw10-course-work\Auth\kubernates\auth-service\values.yaml  
- .\hw10-course-work\Auth\kubernates\auth-service\templates\config.yaml  
- .\hw10-course-work-auth\templates\deployment.yaml  
- .\hw10-course-work\Auth\kubernates\auth-service\templates\_helpers.tpl  
Добавлено:  
```
{{- define "postgresql.fullname" -}}
{{- printf "%s-%s" .Release.Name "postgresql" | trunc 63 -}}
{{- end -}}
```

### Проверить генерацию templates
```
helm install auth-service .\hw10-course-work\Auth\kubernates\auth-service --dry-run
```
## Удалить предыдущую установку 
```
helm uninstall auth-service
```

## Установить приложение
```
helm install auth-service .\hw10-course-work\Auth\kubernates\auth-service
```

## Разное 
### Определение доменного имени сервиса
***На основе***: https://medium.com/kubernetes-tutorials/kubernetes-dns-for-services-and-pods-664804211501
kubectl exec -it hw10-course-work-auth-6b5f58fb87-jtrct -- sh
PING hw10-course-work-auth.default.svc.cluster.local (10.97.121.80): 56 data bytes

***Note***
Как выполнять debug контейнера
https://kubernetes.io/docs/tasks/debug-application-cluster/debug-running-pod/
