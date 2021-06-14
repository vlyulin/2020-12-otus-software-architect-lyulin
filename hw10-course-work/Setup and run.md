# Установка и запуск приложения hw10-course-work

## Установка ingress-nginx
***Note***: шаг выполняется, если требуется

Проверить наличие ingress
You can check for pods implementing ingress controllers (actually with ingress in the name) with:
```
kubectl get pods --all-namespaces | grep ingress
```
And services exposing them with:
```
kubectl get service --all-namespaces | grep ingress
```

Выполнить команды:
```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx
```

## Установить Postgres

Из директории .\2020-12-otus-software-architect-lyulin выполнить команду:
```
helm install cw-postgres -f ./hw10-course-work/postgresql/values.yaml bitnami/postgresql
```
### Проверить работоспособность через Port forward
```
kubectl port-forward --namespace default svc/cw-postgres-postgresql 5432:5432
```

##Установить сервис order-service
```
helm install order-service ./hw10-course-work/OrderService/kubernates/order-service
```

## Установить сервис auth-service
```
helm install auth-service .\hw10-course-work\Auth\kubernates\auth-service
```

##Установить сервис billing-service
```
helm install billing-service ./hw10-course-work/BillingService/kubernates/billing-service
```

##Установить сервис notification-service
```
helm install notification-service ./hw10-course-work/NotificationService/kubernates/notification-service
```

##Тестирование с помощью скриптов Postman

Выполнить import скриптов ./hw10-course-work/postman/hw10-course-work.postman_collection.json в Postman и выполнить тесты

##Тестирование с помощью Newman

newman run ./hw10-course-work/postman/hw10-course-work.postman_collection

##Тестирование с помощью Newman и newman-reporter-htmlextra

### Установить newman-reporter-htmlextra
***Note***Источник: https://www.npmjs.com/package/newman-reporter-htmlextra
Выполнить команду:
```
npm install -g newman-reporter-htmlextra
```
### Выполнить тесты

Выполнить команду:
```
newman run ./hw10-course-work/postman/hw10-course-work.postman_collection.json -r htmlextra
```
В текущей директории будет создана папка newman с отчетом о результате выполнения тестов.

Результат представлен в файле ./hw10-course-work/postman/newman/hw10-course-work-2021-06-08-17-19-26-918-0.html 

------

## Установить Zipkin сервер

### Установить Zipkin сервер
kubectl apply -f .\zipkin-server-pod.yaml
kubectl apply -f .\zipkin-service.yaml

### Port forward
```
kubectl port-forward --namespace default svc/zipkin-service 9411:9411
```

Альтернатива

```
docker run -d -p 9411:9411 openzipkin/zipkin-slim
```
