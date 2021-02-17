


## Установка minikubes

> Инструкция: https://kubernetes.io/ru/docs/setup/learning-environment/minikube/#%D1%83%D0%BA%D0%B0%D0%B7%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B4%D1%80%D0%B0%D0%B9%D0%B2%D0%B5%D1%80%D0%B0-%D0%B2%D0%B8%D1%80%D1%82%D1%83%D0%B0%D0%BB%D1%8C%D0%BD%D0%BE%D0%B9-%D0%BC%D0%B0%D1%88%D0%B8%D0%BD%D1%8B
1. Скачать minikubes по ссылке https://github.com/kubernetes/minikube/releases/latest/download/minikube-installer.exe
2. Установить: minikube start --vm-driver=docker
3. Проверка установки

    `minikube status`

## Создать namespace
kubectl create namespace monitoring

## Установить Helm

Инструкция по установке Helm:  
https://github.com/helm/helm/releases/latest

## Установка Prometheus
### Добавить репозиторий c helm chart Prometheus
Инструкция на: https://github.com/prometheus-community/helm-charts

`helm repo add prometheus-community https://prometheus-community.github.io/helm-charts`

### Добавить helm репозитории
Перейти в папку \2020-12-otus-software-architect-lyulin\hw03-Prometheus\prometheus\ 
Выполнить команду
`helm repo add prometheus-community`

`helm repo add stable https://charts.helm.sh/stable`

`helm repo update`

helm create hw03-Prometheus

https://technology.amis.nl/devops/first-steps-with-prometheus-and-grafana-on-kubernetes-on-windows/
--------
git clone https://github.com/coreos/prometheus-operator
kubectl apply -f bundle.yaml
`kubectl get po`
![Prometheus operator](./images/installed_prometheus_operator.png "Prometheus operator")

## Установка Web UI (Dashboard)
Инструкция: https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/
Установка:
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml
Создание пользователя:
Инструкция: https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md

Creating a Service Account
kubectl apply -f ./kubernates-dashboard/ServiceAccount.yaml

Creating a ClusterRoleBinding
kubectl apply -f ./kubernates-dashboard/ClusterRoleBinding.yaml

Getting a Bearer Token
kubectl -n kubernetes-dashboard get secret $(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}") -o go-template="{{.data.token | base64decode}}" > ./kubernates-dashboard/token

Для доступа к Dashboard
kubectl proxy

Заходим через броузер
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
и в поле Token вводим полученный токен.

Accessing the Dashboard UI
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

Заходим в dashboard и видим prometheus-operator
![Prometheus operator](./images/prometheus-operator.png "Kubectl dashboard")	

Для удаления:
kubectl -n kubernetes-dashboard delete serviceaccount admin-user
kubectl -n kubernetes-dashboard delete clusterrolebinding admin-user
