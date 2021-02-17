


## ��������� minikubes

> ����������: https://kubernetes.io/ru/docs/setup/learning-environment/minikube/#%D1%83%D0%BA%D0%B0%D0%B7%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B4%D1%80%D0%B0%D0%B9%D0%B2%D0%B5%D1%80%D0%B0-%D0%B2%D0%B8%D1%80%D1%82%D1%83%D0%B0%D0%BB%D1%8C%D0%BD%D0%BE%D0%B9-%D0%BC%D0%B0%D1%88%D0%B8%D0%BD%D1%8B
1. ������� minikubes �� ������ https://github.com/kubernetes/minikube/releases/latest/download/minikube-installer.exe
2. ����������: minikube start --vm-driver=docker
3. �������� ���������

    `minikube status`

## ������� namespace
kubectl create namespace monitoring

## ���������� Helm

���������� �� ��������� Helm:  
https://github.com/helm/helm/releases/latest

## ��������� Prometheus
### �������� ����������� c helm chart Prometheus
���������� ��: https://github.com/prometheus-community/helm-charts

`helm repo add prometheus-community https://prometheus-community.github.io/helm-charts`

### �������� helm �����������
������� � ����� \2020-12-otus-software-architect-lyulin\hw03-Prometheus\prometheus\ 
��������� �������
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

## ��������� Web UI (Dashboard)
����������: https://kubernetes.io/docs/tasks/access-application-cluster/web-ui-dashboard/
���������:
kubectl apply -f https://raw.githubusercontent.com/kubernetes/dashboard/v2.0.0/aio/deploy/recommended.yaml
�������� ������������:
����������: https://github.com/kubernetes/dashboard/blob/master/docs/user/access-control/creating-sample-user.md

Creating a Service Account
kubectl apply -f ./kubernates-dashboard/ServiceAccount.yaml

Creating a ClusterRoleBinding
kubectl apply -f ./kubernates-dashboard/ClusterRoleBinding.yaml

Getting a Bearer Token
kubectl -n kubernetes-dashboard get secret $(kubectl -n kubernetes-dashboard get sa/admin-user -o jsonpath="{.secrets[0].name}") -o go-template="{{.data.token | base64decode}}" > ./kubernates-dashboard/token

��� ������� � Dashboard
kubectl proxy

������� ����� �������
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/
� � ���� Token ������ ���������� �����.

Accessing the Dashboard UI
http://localhost:8001/api/v1/namespaces/kubernetes-dashboard/services/https:kubernetes-dashboard:/proxy/

������� � dashboard � ����� prometheus-operator
![Prometheus operator](./images/prometheus-operator.png "Kubectl dashboard")	

��� ��������:
kubectl -n kubernetes-dashboard delete serviceaccount admin-user
kubectl -n kubernetes-dashboard delete clusterrolebinding admin-user
