# Шаги:

## Проверка сборки приложения

Из проекта 2020-12-otus-software-architect-lyulin
выполнить команду: 

`gradle :hw01-Kubernetes:build --no-daemon`  
или  
`build-hw1-kubernates.bat` 

## Сборка docker-файла с приложением

Из проекта 2020-12-otus-software-architect-lyulin выполнить команду:  
`Docker image build --rm --file ./hw01-Kubernetes/Dockerfile.hw01-Kubernetes -t hw1-health-app .`
Или  
`make-image-hw1-health-app.bat` 

## Проверка работоспособности созданного image hw1-health-app

Выполнить команду: `docker run --publish 8080:8080 hw1-health-app` 
или из директории hw01-Kubernetes выполнить `run_hw1-health-app.bat`

В browser: 
http://localhost:8080/hw01-Kubernetes/health
http://localhost:8080/hw01-Kubernetes/sometext

## Разместить образ в DockerHub

```docker images
docker tag <Image ID для hw1-health-app> vlyulin/hw1-health-app:latest
docker login
docker push vlyulin/hw1-health-app
```
В DockerHub появится репозиторий vlyulin/hw1-health-app

Сделано по инструкции: https://dker.ru/docs/docker-engine/get-started-with-docker/tag-push-pull-your-image/

Public view: https://hub.docker.com/r/vlyulin/hw1-health-app

## Установка minikubes

> Инструкция: https://kubernetes.io/ru/docs/setup/learning-environment/minikube/#%D1%83%D0%BA%D0%B0%D0%B7%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B4%D1%80%D0%B0%D0%B9%D0%B2%D0%B5%D1%80%D0%B0-%D0%B2%D0%B8%D1%80%D1%82%D1%83%D0%B0%D0%BB%D1%8C%D0%BD%D0%BE%D0%B9-%D0%BC%D0%B0%D1%88%D0%B8%D0%BD%D1%8B
1. Скачать minikubes по ссылке https://github.com/kubernetes/minikube/releases/latest/download/minikube-installer.exe
2. Установить: minikube start --vm-driver=docker
3. Проверка установки

    `minikube status`
    
## Kubernates

## Добавление Ingress

minikube addons enable ingress

Проверить установку

minikube kubectl get pods -A | grep ingress
 
minikube kubectl get svc -A | grep ingress

### развертывание приложения

```minikube kubectl -- apply -f .\hw1-health-app-deployment.yaml -f .\hw1-health-app-service.yaml -f .\hw1-health-app-ingress.yaml```

### Проверка работоспособности

```minikube kubectl -- port-forward service/hw1-health-app-service 80:8000```

Не прекращая её работу в броузере ввести  
http://localhost/hw01-Kubernetes/health

### Проверяем ingress

Касается windows

Найти ingress-nginx-controller pod'y командой:  
`kubectl get pods -A | grep ingress`

Вставить имя этой pod'ы в команду  
`kubectl port-forward -n kube-system pod/<ingress nginx pod> 80:80`

Например:   
`kubectl port-forward -n kube-system pod/ingress-nginx-controller-558664778f-b4k8s 80:80`

Примечание: процесс остается висеть

В etc/host добавить строку  
`127.0.0.1 arch.homework`

Проверяем:  
`curl -v http://arch.homework/hw01-Kubernetes/health`  
или  
`http://arch.homework/hw01-Kubernetes/health`



