
# Архитектура решения

![components.png](./README.assets/components.png)

## Очистка kubernates от предыдущих задач

Удалить все
```
kubectl delete secrets
kubectl delete pod,svc,deploy --all
```

## Добавление Ingress

По документации: https://kubernetes.github.io/ingress-nginx/deploy/#using-helm
```
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update

helm install ingress-nginx ingress-nginx/ingress-nginx
```

Проверить установку ingress командой:  
`kubectl get pods -A | grep ingress`

#Установка Postgres для тестирования не в docker
```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install hw05-postgres2 --set postgresqlPassword=pswd bitnami/postgresql 
```

## Добыть пароль для postgres под Windows
echo $(kubectl get secret --namespace default hw05-postgres-postgresql -o jsonpath="{.data.postgresql-password}")
Декодировать или через https://www.base64decode.org/ или с помощью утилиты certutil -decode in.txt out.txt (пароль надо записать в файл)
User: postgres

## Для доступа к базе сделать forward портов
```
kubectl port-forward --namespace default svc/postgresql 5432:5432
```

## Установка Postgres для приложения
см. .\hw05-Apigateway\postgresql\Readme.md

## Установить приложение Auth приложение
см. \hw05-Apigateway\Auth\Readme.md

## Установить приложение App
см. \hw05-Apigateway\App\Readme.md

## Установка newman
Установка описана по ссылке: 
https://blog.postman.com/installing-newman-on-windows/

Вкратце: 
Проверить установку npm
```
npm -v
```

Установить newman
```
npm install -g newman
```
