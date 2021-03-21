# Установка и запуск приложения hw05-Apigateway

## Установка ingress-nginx

***Note***: шаг выполняется, если требуется
Выполнить команды:
```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update

helm install ingress-nginx ingress-nginx/ingress-nginx
```

## Установить Postgres

Выполнить команду:
```
helm install hw05-postgres2 -f ./hw05-Apigateway/postgresql/values.yaml bitnami/postgresql
```

##Установить приложение для аутентификации
helm install --replace hw05-apigateway-auth ./hw05-Apigateway/Auth/kubernates/hw05-apigateway-auth

##Установить приложение "Репозиторий пользователей"
helm install --replace hw05-apigateway-app ./hw05-Apigateway/App/kubernates/hw05-apigateway-app

##Тестирование с помощью скриптов Postman

Выполнить import скриптов .\hw05-Apigateway\Postman\hw05-Apigateway.postman_collection_v2.1 в Postman и выполнить тесты

##Тестирование с помощью Newman

newman run .\hw05-Apigateway\Postman\hw05-Apigateway.postman_collection_v2.1 

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
newman run .\hw05-Apigateway\Postman\hw05-Apigateway.postman_collection_v2.1 -r htmlextra
```
В текущей директории будет создана папка newman с отчетом о результате выполнения тестов.