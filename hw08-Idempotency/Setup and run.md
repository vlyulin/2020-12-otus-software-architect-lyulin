# Установка и запуск приложения hw08-Idempotency

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
helm install hw08-postgres -f ./hw08-Idempotency/postgresql/values.yaml bitnami/postgresql
```
### Проверить работоспособность через Port forward
```
kubectl port-forward --namespace default svc/hw08-postgres-postgresql 5432:5432
```

##Установить приложение для аутентификации
helm install --replace hw08-idempotency-auth ./hw08-Idempotency/Auth/kubernates/hw08-idempotency-auth

##Установить приложение
helm install --replace hw08-idempotency-app ./hw08-Idempotency/App/kubernates/hw08-idempotency-app

##Тестирование с помощью скриптов Postman

Выполнить import скриптов .\hw08-Idempotency\Postman\hw08-Idempotency.postman_collection_v2.1 в Postman и выполнить тесты

##Тестирование с помощью Newman

newman run .\hw08-Idempotency\Postman\hw08-Idempotency.postman_collection_v2.1 

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
newman run .\hw08-Idempotency\Postman\hw08-Idempotency.postman_collection_v2.1 -r htmlextra
```
В текущей директории будет создана папка newman с отчетом о результате выполнения тестов.
