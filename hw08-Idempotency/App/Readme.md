# Шаги:

## Собрать приложение

Из директории проекта 2020-12-otus-software-architect-lyulin
выполнить команду: 

```
gradlew :hw08-Idempotency:App:build -x test
или
gradle :hw08-Idempotency:App:build -x test
```

Проверить запуск приложения
```
java -DDB_DRIVER=org.postgresql.Driver -DDB_URL=jdbc:postgresql://localhost:5432/postgres -DDB_USERNAME=postgresadmin -DDB_PASSWORD=pswd -jar ./hw08-Idempotency/App/build/libs/App-1.0.0.jar
```

## Собрать docker-файла с приложением

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду:  
```
docker build -f ./hw08-Idempotency/App/Dockerfile.hw08-Idempotency-app --build-arg JAR_FILE=./hw08-Idempotency/App/build/libs/*.jar -t vlyulin/hw08-idempotency-app .
docker build -f Dockerfile.hw08-Idempotency-app --build-arg JAR_FILE=./build/libs/App-1.0.0.jar -t vlyulin/hw08-idempotency-app .
```  
***None***: -t vlyulin/hw08-Idempotency-app сразу установит tag  

### Проверить запуск приложения из docker
```

kubectl get svc hw08-postgres-postgresql
Cluster IP 10.104.96.225
Указать Cluster IP в env.txt: DB_URL=jdbc:postgresql://10.104.96.225:5432/postgres
docker run --env-file env.txt -p 8080:8080 -it vlyulin/hw08-idempotency-app 
```
***Note***: --net=host - https://stackoverflow.com/questions/37482716/using-docker-to-launch-web-app-cant-connect-to-postgresql-db

## Разместить образ в DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/hw08-idempotency-app
```

## Создать Helm chart для приложения hw02-library-app
mkdir .\hw08-Idempotency\App\kubernates\
cd .\hw08-Idempotency\App\kubernates\
helm create hw08-idempotency-app

Указать настройки в values.yaml
```               
externalPostgresql:
  fullname: hw08-postgres-postgresql.default
  postgresqlHost: hw08-postgres-postgresql
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
    nginx.ingress.kubernetes.io/auth-url: "http://$host/auth"
    nginx.ingress.kubernetes.io/auth-signin: "http://$host/auth/signin"
    nginx.ingress.kubernetes.io/auth-response-headers: "X-User,X-Email,X-UserId,X-First-Name,X-Last-Name"
    nginx.ingress.kubernetes.io/rewrite-target: /$2
```
***Notes*** Обязательно поменять ingress: enabled: false на enabled: true

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
helm install hw08-idempotency-app ./hw08-idempotency-app --dry-run
```
## Удалить предыдущую установку 
```
helm uninstall hw08-Idempotency-app
```
## Установить приложение
```
helm install hw08-idempotency-app ./hw08-Idempotency/App/kubernates/hw08-idempotency-app  
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
kubectl port-forward svc/hw08-postgres2-postgresql 5432:5432
#

Добраться до logs
kubectl exec <POD-NAME> -c <CONTAINER-NAME> -- <COMMAND>
kubectl exec --stdin --tty hw08-idempotency-auth-585f5958c6-ls9jg -- /bin/bash
kubectl exec --stdin --tty hw08-idempotency-app-6498cb4d59-rgwp7  -- /bin/sh
kubectl logs hw08-Idempotency-app-594fd56548-dnz5l

kubectl port-forward po/hw08-Idempotency-app-54f6dfdbf4-d8vj2 8080:8080
kubectl port-forward svc/hw08-Idempotency-app 80:80

Тут про leaveness and readness
https://stackoverflow.com/questions/61637218/spring-boot-app-not-working-on-kubernetes-cluster


## Debug
Запустить docker с image  
docker run -it --rm -p 8787:8080 -v App-1.0.0.war:/usr/local/tomcat/webapps/App-1.0.0.war tomcat:10-jdk8  
docker run -it --rm -p 8787:8080 -v hw03-Prometheus-3.0.0.war:/usr/local/tomcat/webapps/hw03.war tomcat:10-jdk8  
docker run -it --rm -p 8080:8080 vlyulin/hw08-Idempotency-app:latest  

Список контейнеров  
docker ps  
Зайти в контейнер  
docker exec -it e5fc62bc4eee /bin/bash

Проверка nslookup:  
kubectl run busybox --image busybox:1.28 --restart=Never --rm -it busybox -- sh
https://stackoverflow.com/questions/52109039/nslookup-cant-resolve-kubernetes-default
https://kubernetes.io/docs/tasks/administer-cluster/dns-debugging-resolution/
