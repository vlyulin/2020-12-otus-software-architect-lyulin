# Шаги:

## Проверка сборки приложения

Из директории проекта 2020-12-otus-software-architect-lyulin
выполнить команду: 

```gradle :hw02-Kubernetes:build --no-daemon```
или
build-hw02-kubernates.bat

## Сборка docker-файла с приложением

Из директории проекта 2020-12-otus-software-architect-lyulin выполнить команду:  
`Docker image build --rm --file ./hw02-Kubernetes/Dockerfile.hw02-Kubernetes -t hw02-library-app .`  
Или  
`make-image-hw02-library-app.bat` 

## Разместить образ в DockerHub https://hub.docker.com/

```
docker images
docker tag <Image ID для hw1-health-app> vlyulin/hw02-library-app:latest
docker tag ca009361582a vlyulin/hw02-library-app:latest
docker login
docker push vlyulin/hw02-library-app
```
В DockerHub появится репозиторий vlyulin/hw02-library-app

Сделано по инструкции: https://dker.ru/docs/docker-engine/get-started-with-docker/tag-push-pull-your-image/

Public view: https://hub.docker.com/repository/docker/vlyulin/hw02-library-app

## Запустить minikube
`minikube start`

## Добавление Ingress
`minikube addons enable ingress`
Проверить установку ingress командой:  
`kubectl get pods -A | grep ingress`

## Установить Helm

Инструкция по установке Helm:  
https://github.com/helm/helm/releases/latest

### Добавить репозиторий bitnami, где хранятся официальные charts
helm repo add bitnami https://charts.bitnami.com/bitnami

## Создать chart для приложения hw02-library-app
cd ./hw02-Kubernetes/kubernetes
helm create hw02-library-app

### Добавить в чарте зависимость postgresql. 

Изменения вносятся в hw02-library-helm\hw02-library-app\Chart.yaml  
```
dependencies:
    - name: postgresql
      version: 10.0.0
      repository: https://charts.bitnami.com/bitnami
      condition: postgresql.enabled
      tags:
        - hw02-postgres
```

**Note:** version - это версия чарта, а не приложения.  


Устанавливаем указанную зависимости. Они складываются в директорию charts/
```helm dependency update ./hw02-library-app```

## Внесение изменений в templates

### .\2020-12-otus-software-architect-lyulin\hw02-Kubernetes\kubernetes\hw02-library-app\values.yaml 

Указать собранный образ размещенный на Docker hub:  
```
image:
  repository: vlyulin/hw02-library-app
```
Настраиваем сервис для приложения:
```
service:
  type: NodePort
  port: 80
  externalPort: 8080
  targetPort: http
```
Настраиваем ingress:
```
ingress:
  enabled: true
  annotations: 
    nginx.ingress.kubernetes.io/rewrite-target: /hw02-Kubernetes/$2
  hosts:
    - host: arch.homework
      paths: 
        - path: /()(.*)
          pathType: Prefix
          backend:
            service:
              name: hw02-library-app-service
              port:
                name: web
        - path: /otusapp/vlyulin($|/)(.*)
          pathType: Prefix
          backend:
            service:
              name: hw02-library-app-service
              port:
                name: web
        - path: /hw02-Kubernetes/()(.*)
          pathType: Prefix
          backend:
            service:
              name: hw02-library-app-service
              port:
                name: web
```                
***Note:*** path: - это что будет указано в броузере, а "nginx.ingress.kubernetes.io/rewrite-target: /hw02-Kubernetes/$2" - это путь, который будет вызван на сервисе hw02-library-app-service

***Note:*** "path: /hw02-Kubernetes/()(.*)" понадобился из-за того, что при переходе по ссылкам в приложении формировался путь http://arch.homework/hw02-Kubernetes/hw02-Kubernetes/...

Параметры для приложения:
```
externalPostgresql:
  postgresqlDriver: org.postgresql.Driver
  postgresqlUsername: postgresadmin
  postgresqlPassword: pswd
  postgresqlDatabase: postgresdb
  postgresqlHost: "postgres"
  postgresqlPort: "5432"
```
И параметры для subchart postgresql:
```
postgresql:
  enabled: true
  postgresqlUsername: postgresadmin
  postgresqlPassword: pswd
  postgresqlDatabase: postgresdb
  service:
    port: "5432"
```
***Note:*** Возможно, было бы правильнее из совместить.

### ..\2020-12-otus-software-architect-lyulin\hw02-Kubernetes\kubernetes\hw02-library-app\config.yaml 

Создать файл и Добавить секреты:
```
apiVersion: v1
kind: Secret
metadata:
  name: database-uri
type: Opaque
data:
  DATABASE_URI: {{ printf "jdbc:postgresql://%s:%s/%s" (include "postgresql.fullname" .) .Values.externalPostgresql.postgresqlPort .Values.externalPostgresql.postgresqlDatabase  | b64enc | quote }}
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
### ..\2020-12-otus-software-architect-lyulin\hw02-Kubernetes\kubernetes\hw02-library-app\deployment.yaml 

В spec ... template ... spec добавить ожидание, когда поднимется pod с базой postgress

```
initContainers:
        - name: check-db-ready
          image: postgres:latest
          env:
            - name: POSTGRES_HOST
              value: {{ include "postgresql.fullname" . | quote }}
            - name: POSTGRES_PORT
              value: {{ .Values.postgresql.service.port | quote }}
          command:
            - sh
            - "-c"
            - |
              until pg_isready -h $POSTGRES_HOST  -p  $POSTGRES_PORT; 
              do echo waiting for database; sleep 2; done;
```
***Note:*** Непонятно откуда берется {{ postgresql.fullname ...}} 

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
`helm install hw02-library-app ./hw02-library-app --dry-run`  
или  
`.\hw02-Kubernetes\kubernetes\hw02-library-helm\helmInstallHw02LibraryApp-dry-run.bat`

## Установить приложение  
### Удаление предыдущей установки

Get all releases  
`helm ls --all-namespaces`  OR   `helm ls -A`

Delete release  
```
helm uninstall release_name -n release_namespace
helm uninstall hw02-library-app -n hw02
```
### Новая установка

***Note:*** namespace hw02 должен быть создан автоматически при установке приложения, но если что-то пойдет не так, то создать вручную  
```kubectl create namespace hw02```

Из директории .\hw02-Kubernetes\kubernetes
Команда установки приложения:  
```
helm install --replace --namespace=hw02 hw02-library-app ./hw02-library-app  
или
.\helmInstallHw02LibraryApp.bat
```

Если при повторной установке получаем ошибку:  
`Error: cannot re-use a name that is still in use`    
то находим секрет  
`kubectl get secret --all-namespaces -l "owner=helm"`  
удаляем его  
`kubectl delete secret sh.helm.release.v1.hw02-library-app.v1 -n hw02`  
и запускаем установку снова  
***Note:*** Рецепт найден тут: https://github.com/helm/helm/issues/4174  

Дождаться, когда pod hw02-library-app-<some hash> будет в состоянии READY  
Проверяется командой:
`kubectl get po -n hw02`

## Проверка работоспособности приложения

### Под Win 
В etc/host добавить строку  
`127.0.0.1 arch.homework`

Cделать forward на Ingress
Найти ingress-nginx-controller pod'y командой:  
`kubectl get pods -A --namespace hw02 | grep ingress`
Вставить имя этой pod'ы в команду  
```
kubectl port-forward -n kube-system pod/<ingress nginx pod> 80:80`
пример: 
kubectl port-forward -n kube-system pod/ingress-nginx-controller-558664778f-sdzs7 80:80
```

## URLs:
```
http://arch.homework/
http://arch.homework/health
http://arch.homework/vlyulin/health
```
### Логины

user: Admin pswd: 12345678  
user: User01 pswd: 12345678  
user: User02 pswd: 12345678  

---
## Записки по debug pods

Находим pod с приложением  
`kubectl get pods --namespace hw02`
Делаем на него форвард  
`kubectl port-forward -n hw02 pod/<pod> 80:8080`
kubectl port-forward -n hw02 pod/hw02-library-app-598d987447-dpdqc 80:8080

kubectl port-forward -n hw02 service/hw02-library-app-postgresql 5432:5432

Посмотреть логи  
kubectl -n hw02 logs pod/hw02-library-app-79cd7b96b8-cjxmn

kubectl describe pod

Посмотреть, что там внутри:  
```
kubectl exec <POD-NAME> -c <CONTAINER-NAME> -- <COMMAND>
kubectl exec --namespace="hw02" hw02-library-app-79cd7b96b8-gccnr -- ls /
docker exec -it hw02-library-app-79cd7b96b8-gccnr /bin/bash
kubectl exec --stdin --tty hw02-library-app-79cd7b96b8-gccnr -n hw02 -- /bin/bash
```

## Удаление релиза
#### Get all releases
```
helm ls --all-namespaces  
OR
helm ls -A
```
#### Delete release
```
helm uninstall release_name -n release_namespace
helm uninstall hw02-library-app -n hw02
```
Examples:  
`helm uninstall hw02-library-app -n hw02`






---
## Полезные ссылки
Subcharts
https://helm.sh/docs/chart_template_guide/subcharts_and_globals/

Синтаксис
https://helm.sh/docs/chart_template_guide/yaml_techniques/


Одинокая установка Postgres:
Перейти в диреторию .\kubernetes  
Charts: https://github.com/bitnami/charts/tree/master/bitnami/postgresql  
Установка postgres:  
```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install hw02-postgres -f values.yaml bitnami/postgresql --namespace hw02  
```
или 
`HelmInstallPostgres.bat` 

а можно как-то так:
helm install hw02-postgres bitnami/postgresql --namespace hw02
