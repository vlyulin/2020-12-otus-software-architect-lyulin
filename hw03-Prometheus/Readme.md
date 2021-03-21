@ ## ��������� minikubes

> ����������: https://kubernetes.io/ru/docs/setup/learning-environment/minikube/#%D1%83%D0%BA%D0%B0%D0%B7%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B4%D1%80%D0%B0%D0%B9%D0%B2%D0%B5%D1%80%D0%B0-%D0%B2%D0%B8%D1%80%D1%82%D1%83%D0%B0%D0%BB%D1%8C%D0%BD%D0%BE%D0%B9-%D0%BC%D0%B0%D1%88%D0%B8%D0%BD%D1%8B
1. ������� minikubes �� ������ https://github.com/kubernetes/minikube/releases/latest/download/minikube-installer.exe
2. ����������: minikube start --vm-driver=docker
3. �������� ���������

    `minikube status`

## ������� namespace
@ kubectl create namespace monitoring

## ���������� Helm

���������� �� ��������� Helm:  
https://github.com/helm/helm/releases/latest

## ��������� Prometheus

������� ����� ..\2020-12-otus-software-architect-lyulin\hw03-Prometheus\prometheus 
��������� �������
`git clone https://github.com/coreos/prometheus-operator`
������� � ����� .\2020-12-otus-software-architect-lyulin\hw03-Prometheus\prometheus\prometheus-operator\ 
��������� ��������� prometheus ��������:
`kubectl apply -f bundle.yaml`
��������� ��������� ��������:
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

-------------------

��� �� ����

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
������ ����������:

��������� ����� � ���

## ���������� ����� � DockerHub https://hub.docker.com/

```
docker images
docker tag <Image ID ��� hw1-health-app> vlyulin/hw03-library-app:latest
docker login
docker push vlyulin/hw03-library-app
```
� DockerHub �������� ����������� vlyulin/hw02-library-app

## ���������� Ingress
`minikube addons enable ingress`
��������� ��������� ingress ��������:  
`kubectl get pods -A | grep ingress`

## ��������� Helm
��������:
https://github.com/helm/helm/releases/tag/v3.5.2
������� � ��������� ������ ���������:
https://raw.githubusercontent.com/helm/helm/master/scripts/get-helm-3

### �������� ����������� bitnami, ��� �������� ����������� charts
`helm repo add bitnami https://charts.bitnami.com/bitnami`

#���������� ���������� hw03-library-app
������� � ���������� 2020-12-otus-software-architect-lyulin\hw03-Kubernetes\kubernetes\hw03-library-app
��������� �������
```
kubectl create namespace hw03
helm install --namespace=hw03 hw03-library-app ./hw03-library-app
--replace
��� ��������� ������
install-hw03-library-app.sh
```

���� �������� �� postgresql-10.0.0.tgz, �� ������� ����� � ��������� ��������� ��������:
������� � ���������� 2020-12-otus-software-architect-lyulin\hw03-Kubernetes\kubernetes\hw03-library-app
��������� �������:
```
helm dependency update hw03-library-app/
```

��������� ���������:
```
kubectl get po -n hw03
```
���������� ����:
```
kubectl -n hw03 logs pod/<��� ����>
```

���������� ��������� ������� ������ ����:
```
kubectl get all -l app.kubernetes.io/instance=hw03-library-app -n hw03
helm list -n hw03 | grep hw03-library-app
```
��������� ������ ������:
��� PowerShell
Invoke-RestMethod -Uri http://arch.homework/prometheus
https://docs.microsoft.com/en-us/powershell/module/Microsoft.powershell.utility/invoke-restmethod?view=powershell-7.1

# ��������� ����������� � Prometheus
������� ���� ServiceMonitor � ����������
.\2020-12-otus-software-architect-lyulin\hw03-Prometheus\kubernates\hw03-library-app\templates\servicemonitor.yaml 

endpoint ������ ���� �����:
endpoints:
  - interval: 5s
    port: http
    path: hw03-Prometheus-3.0.0/prometheus

�����, ��� ����� ���:
kubectl create -f ./servicemonitor.yaml

��������� ��������� ����� � ���� values.yaml 
metrics:
  serviceMonitor:
    enabled: true

��������� �����
`helm upgrade hw03-library-app ./hw03-library-app -n hw03 --atomic`
��� --atomic - if set, upgrade process rolls back changes made in case of failed upgrade. The --wait flag will be set automatically if --atomic is used
��� �� ������������ https://helm.sh/docs/helm/helm_upgrade/

�������, ��� ������-������� ��������
kubectl get servicemonitors.monitoring.coreos.com -n monitoring

������� ��� ������
kubectl describe servicemonitors.monitoring.coreos.com hw03-library-app -n hw03

��������� ��������� �� Prometheus ������ ��������� �������:
kubectl get -n monitoring prometheus -o yaml
��� monitoring ��� ��������� ���� ���������� ���������.
������ ������, �������� serviceMonitorSelector.matchLabels: ��� ����� ������ �� ������� ������ ���� ServiceMonitor ����������

� ����� .\2020-12-otus-software-architect-lyulin\hw03-Prometheus\kubernates\hw03-library-app\templates\servicemonitor.yaml
������� 
metadata:
  name: hw03-library-app
��
metadata:
  name: prom

���������� �� servicemonitor
kubectl describe servicemonitor -n hw03

����������� Prometheus
curl -X POST http://localhost:9090/-/reload
Invoke-WebRequest -Uri http://localhost:9090/-/reload -Method 'POST'

======
������ 
.\2020-12-otus-software-architect-lyulin\hw03-Prometheus\kubernates\servicemonitor2.yaml 
��������� � namespace monitoring:
kubectl apply -f servicemonitor2.yaml -n monitoring

�� ���������: https://kruschecompany.com/kubernetes-prometheus-operator/
-------
��������, to see what metrics are going to be needed to run the command:
kubectl get servicemonitors.monitoring.coreos.com -n monitoring
-------
Now deployServiceMonitors. Prometheus discovers ServiceMonitors by label. You need to know which ServiceMonitors label it is looking for. To do this:
kubectl get prometheuses.monitoring.coreos.com -oyaml

## �������� ������
#### Get all releases
```
helm ls --all-namespaces  
OR
helm ls -A
```

#### Delete release
```
helm uninstall release_name -n release_namespace
helm uninstall hw03-library-app -n hw03
```


------
�������� ������
https://github.com/mkjelland/spring-boot-postgres-on-k8s-sample

------

�������� � ���������� ������� Prometheus

������� ���:
https://njalnordmark.wordpress.com/2017/05/08/using-prometheus-with-spring-boot/
��� ����������!


------
������� � Docker Desktop � ���������� Kubernates
 
��������� ingress
kubectl describe ing hw03-library-app -n hw03

�����, ��� ���� Address: ������
Name:             hw03-library-app
Namespace:        hw03
Address:
Default backend:  default-http-backend:80 (<none>)

�������, �� ���������� nginx-ingress

��������� nginx-ingress
https://kubernetes.github.io/ingress-nginx/deploy/#using-helm
helm repo add ingress-nginx https://kubernetes.github.io/ingress-nginx
helm repo update
helm install ingress-nginx ingress-nginx/ingress-nginx

�������� ����� ���������:
kubectl describe ing hw03-library-app -n hw03

Name:             hw03-library-app
Namespace:        hw03
Address:          localhost
Default backend:  default-http-backend:80 (<none>)

������ ��������.

-------
����� � pod
kubectl exec hw03-library-app-65c7dbbfdc-95288 -n hw03 -ti -- bash

-------

# ��������� prometheus
# ��������: https://github.com/prometheus-community/helm-charts/tree/main/charts/kube-prometheus-stack

# helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
# helm repo add stable https://charts.helm.sh/stable
# helm repo update

���������� ������ prometheus-community:
helm search repo prometheus-community/kube-prometheus-stack --versions

���������� prometheus:
# helm install 13.11.0 prometheus-community/kube-prometheus-stack
# ����� ���������� � --namespace monitoring
# 13.10.0
# helm install 13.10.0 prometheus-community/kube-prometheus-stack

kubectl create namespace monitoring
kubectl config set-context --current --namespace=monitoring

helm repo add prometheus-community https://prometheus-community.github.io/helm-charts
helm repo add stable https://charts.helm.sh/stable
helm repo update

helm install prom prometheus-community/kube-prometheus-stack 
����� ����� 
helm install prometheus-community/kube-prometheus-stack --generate-name

kube-prometheus-stack has been installed. Check its status by running:
  kubectl --namespace monitoring get pods -l "release=prom"

���������� pods
kubectl --namespace monitoring get pods

������ � Graphana
kubectl port-forward -n monitoring service/prom-grafana 9000:80
http://localhost:9000/dashboard/new?orgId=1
admin/... ���-�� � ����� ��� � google-doke

������ � Prometheus:
kubectl port-forward -n monitoring service/prom-kube-prometheus-stack-prometheus 9090

-------------
��������� ab ��� Windows ��� ������������ ������������
https://www.cedric-dumont.com/2017/02/01/install-apache-benchmarking-tool-ab-on-windows/

while 1; do c:\Utils\ab -n 50 -c 5 http://arch.homework/users ; sleep 3; done

while(true){
    c:\Utils\ab -n 50 -c 5 http://arch.homework/users
    sleep 3
}


### Ingress and Graphana
#### ������� � Ingress-��

��� ����, ��� nginx ����� �������� �������, ���������� ��� ��� ��������� � ����������.
��� ����� ��������� ���� nginx-ingress.yaml

��� ������� metrics.serviceMonitor.enabled=true
��� �������
```
  metadata:
      name: prom-daemonset
      # hw03-library-app
      labels:
        release: prom
```

������ ������� ����� nginx-ingress ����� helm
helm list
������� name ��� ingress. � ���� ������ ingress-nginx
��������� ��� ��� � �������� [RELEASE] � ������� helm upgrade
`helm upgrade ingress-nginx ingress-nginx/ingress-nginx -f nginx-ingress.yaml`

�������, ��� ������ ������� ���������
kubectl get servicemonitors.monitoring.coreos.com -n hw03
��������:
NAME                                        AGE
hw03-library-app-ingress-nginx-controller   10m

���� ������ ��� ��������� ����������
Error: Internal error occurred: failed calling webhook "validate.nginx.ingress.kubernetes.io": Post "https://ingress-nginx-prom-daemonset-admission.default.
svc:443/networking/v1beta1/ingresses?timeout=10s": dial tcp 10.99.184.10:443: connect: connection refused

kubectl get daemonsets -n hw03
kubectl delete daemonsets/hw03-library-app-ingress-nginx-controller -n hw03

Error: failed to create resource: Internal error occurred: failed calling webhook "validate.nginx.ingress.kubernetes.io": Post "https://ingress-nginx-prom-d
aemonset-admission.default.svc:443/networking/v1beta1/ingresses?timeout=10s": dial tcp 10.99.184.10:443: connect: connection refused

kubectl get validatingwebhookconfiguration
kubectl delete validatingwebhookconfigurations ingress-nginx-admission

����� ����� ������������.
�������������� ingress
helm upgrade ingress-nginx ingress-nginx/ingress-nginx

�����-�� ���� � nginx
helm delete ingress-nginx

Error: failed to create resource: Internal error occurred: failed calling webhook "validate.nginx.ingress.kubernetes.io"

kubectl get validatingwebhookconfigurations 
kubectl delete validatingwebhookconfigurations [configuration-name]
kubectl delete validatingwebhookconfigurations ingress-nginx-admission

------

https://www.digitalocean.com/community/questions/trouble-monitoring-nginx-ingress-with-prometheus
��� ��� ����������. 
������� ���� ������ � ����� ����������� �� namespace:
��
# upgrade ingress to enable metrics
helm upgrade ingress-nginx ingress-nginx/ingress-nginx --namespace default --set controller.metrics.serviceMonitor.enabled=true --set controller.metrics.enabled=true

# upgrade prometheus operator to look in other namespaces
helm upgrade prometheus-operator stable/prometheus-operator --namespace monitoring --set prometheus.prometheusSpec.serviceMonitorSelectorNilUsesHelmValues=false

kubectl get servicemonitors.monitoring.coreos.com
----
helm list -n hw03
helm upgrade hw03-library-app ingress-nginx/ingress-nginx --namespace hw03 --set controller.metrics.serviceMonitor.enabled=true --set controller.metrics.enabled=true

-------------
Graphana

RPC for nginx-ingress
rate(nginx_ingress_controller_request_duration_seconds_count{host="arch.homework"}[1m])

RPC for hw03-library-app
rate(http_server_requests_seconds_count[1m])

200 counter for nginx-ingress
sum by (status) (increase(nginx_ingress_controller_request_duration_seconds_count{status=~"2.+"}[1m]))

200 counter for hw03-library-app
sum by (status) (increase(http_server_requests_seconds_count{status=~"2.+"}[1m]))

-------------
����������:
https://nickjanetakis.com/blog/a-linux-dev-environment-on-windows-with-wsl-2-docker-desktop-and-more

Invoke-WebRequest
https://docs.microsoft.com/en-us/powershell/module/microsoft.powershell.utility/invoke-webrequest?view=powershell-7.1
