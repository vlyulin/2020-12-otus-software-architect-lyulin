# Шаги:

## Собрать приложение

Из директории проекта 2020-12-otus-software-architect-lyulin
выполнить команду: 

```
gradlew :hw05-Apigateway:App:build
или
gradle :hw05-Apigateway:App:build 
```

Проверить запуск приложения
```
java -jar hw05-Apigateway/App/build/libs/App-1.0.0.jar
```

## Собрать docker-файла с приложением

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду:  
```
docker build -f ./hw05-Apigateway/App/Dockerfile.hw05-apigateway-app --build-arg JAR_FILE=./hw05-Apigateway/App/build/libs/*.jar -t vlyulin/hw05-apigateway-app .
```  
***None***: -t vlyulin/hw05-apigateway-app сразу установит tag  

### Проверить запуск приложения из docker
```
docker run -p 8080:8080 -t vlyulin/hw05-apigateway-app
```

## Разместить образ в DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/hw05-apigateway-app
```

## Создать Helm chart для приложения hw02-library-app
cd .\hw05-Apigateway\App\kubernates\
helm create hw05-apigateway-app

Указать настройки в values.yaml
```
externalPostgresql:
  fullname: hw05-postgres2-postgresql.default
  postgresqlHost: hw05-postgres2-postgresql
  postgresqlDriver: org.postgresql.Driver
  postgresqlUsername: postgres
  postgresqlPassword: pswd
  postgresqlDatabase: postgresdb
  postgresqlPort: "5432"
```
В раздел ingress добавить переадресацию на auth service
```angular2html
ingress:
  enabled: true
  annotations:
    nginx.ingress.kubernetes.io/auth-url: "http://hw05-apigateway-auth.default.svc.cluster.local:80/auth"
    nginx.ingress.kubernetes.io/auth-signin: "http://$host/auth/signin"
    nginx.ingress.kubernetes.io/auth-response-headers: "X-User,X-Email,X-UserId,X-First-Name,X-Last-Name"
    nginx.ingress.kubernetes.io/rewrite-target: /$2
```

Создать файл config.yaml и добавить секреты

```
apiVersion: v1
kind: Secret
metadata:
  name: database-uri
type: Opaque
data:
  DATABASE_URI: {{ printf "jdbc:postgresql://%s:%s/%s" .Values.externalPostgresql.postgresqlHost .Values.externalPostgresql.postgresqlPort .Values.externalPostgresql.postgresqlDatabase  | b64enc | quote }}
---
apiVersion: v1
kind: Secret
metadata:
  name: db-username
type: Opaque
data: 
  db_username: {{ .Values.externalPostgresql.postgresqlUsername | b64enc | quote }}
---
apiVersion: v1
kind: Secret
metadata:
  name: db-password
type: Opaque
data: 
  db_password: {{ .Values.externalPostgresql.postgresqlPassword | b64enc | quote }}  
```

### В deployment.yaml 
В раздел spec ... template ... spec добавить ожидание, когда поднимется pod с базой postgress
```
      # My start
      initContainers:
        - name: check-db-ready
          image: postgres:latest
          env:
            - name: POSTGRES_HOST
              value: {{ .Values.externalPostgresql.fullname | quote }}
            - name: POSTGRES_PORT
              value: {{ .Values.externalPostgresql.postgresqlPort | quote }}
          command:
            - sh
            - "-c"
            - |
              until pg_isready -h $POSTGRES_HOST  -p  $POSTGRES_PORT; 
              do echo waiting for database; sleep 2; done;
      # My end
```

Добавить переменные окружения:
```
env:
            - name: DB_URL
              valueFrom:
                secretKeyRef:
                  name: database-uri
                  key: DATABASE_URI
            - name: DB_USERNAME
              valueFrom:
                secretKeyRef:
                  name: db-username
                  key: db_username
            - name: DB_PASSWORD
              valueFrom:
                secretKeyRef:
                  name: db-password
                  key: db_password
            - name: DB_DRIVER
              value: {{ .Values.externalPostgresql.postgresqlDriver }}
            - name: WAIT_FOR
              value: "postgresql:5432"
```

### Проверить генерацию templates
```
helm install hw05-apigateway-app ./hw05-apigateway-app --dry-run
```
## Удалить предыдущую установку 
```
helm uninstall hw05-apigateway-app
```
## Установить приложение
```
helm install --replace hw05-apigateway-app ./hw05-apigateway-app  
```
Cделать forward на Ingress
Найти ingress-nginx-controller pod'y командой:  
```
kubectl get pods -A | grep ingress
```
Вставить имя этой pod'ы в команду
```
kubectl port-forward -n <namespace> pod/<ingress nginx pod> 80:80`
пример: 
kubectl port-forward -n default pod/ingress-nginx-controller-7fc74cf778-xk778 80:80
```

## Разное
kubectl port-forward svc/hw05-postgres2-postgresql 5432:5432
#

Добраться до logs
kubectl exec <POD-NAME> -c <CONTAINER-NAME> -- <COMMAND>
kubectl exec --stdin --tty hw05-apigateway-app-594fd56548-dnz5l -- /bin/bash
kubectl logs hw05-apigateway-app-594fd56548-dnz5l

kubectl port-forward po/hw05-apigateway-app-54f6dfdbf4-d8vj2 8080:8080
kubectl port-forward svc/hw05-apigateway-app 80:80

Тут про leaveness and readness
https://stackoverflow.com/questions/61637218/spring-boot-app-not-working-on-kubernetes-cluster


## Debug
Запустить docker с image  
docker run -it --rm -p 8787:8080 -v App-1.0.0.war:/usr/local/tomcat/webapps/App-1.0.0.war tomcat:10-jdk8  
docker run -it --rm -p 8787:8080 -v hw03-Prometheus-3.0.0.war:/usr/local/tomcat/webapps/hw03.war tomcat:10-jdk8  
docker run -it --rm -p 8080:8080 vlyulin/hw05-apigateway-app:latest  

Список контейнеров  
docker ps  
Зайти в контейнер  
docker exec -it e5fc62bc4eee /bin/bash

Проверка nslookup:  
kubectl run busybox --image busybox:1.28 --restart=Never --rm -it busybox -- sh
https://stackoverflow.com/questions/52109039/nslookup-cant-resolve-kubernetes-default
https://kubernetes.io/docs/tasks/administer-cluster/dns-debugging-resolution/
