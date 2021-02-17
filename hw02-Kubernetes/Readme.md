# ����:

## �������� ������ ����������

�� ���������� ������� 2020-12-otus-software-architect-lyulin
��������� �������: 

```gradle :hw02-Kubernetes:build --no-daemon```
���
build-hw02-kubernates.bat

## ������ docker-����� � �����������

�� ���������� ������� 2020-12-otus-software-architect-lyulin ��������� �������:  
`Docker image build --rm --file ./hw02-Kubernetes/Dockerfile.hw02-Kubernetes -t hw02-library-app .`  
���  
`make-image-hw02-library-app.bat` 

## ���������� ����� � DockerHub https://hub.docker.com/

```
docker images
docker tag <Image ID ��� hw1-health-app> vlyulin/hw02-library-app:latest
docker tag ca009361582a vlyulin/hw02-library-app:latest
docker login
docker push vlyulin/hw02-library-app
```
� DockerHub �������� ����������� vlyulin/hw02-library-app

������� �� ����������: https://dker.ru/docs/docker-engine/get-started-with-docker/tag-push-pull-your-image/

Public view: https://hub.docker.com/repository/docker/vlyulin/hw02-library-app

## ��������� minikube
`minikube start`

## ���������� Ingress
`minikube addons enable ingress`
��������� ��������� ingress ��������:  
`kubectl get pods -A | grep ingress`

## ���������� Helm

���������� �� ��������� Helm:  
https://github.com/helm/helm/releases/latest

### �������� ����������� bitnami, ��� �������� ����������� charts
helm repo add bitnami https://charts.bitnami.com/bitnami

## ������� chart ��� ���������� hw02-library-app
cd ./hw02-Kubernetes/kubernetes
helm create hw02-library-app

### �������� � ����� ����������� postgresql. 

��������� �������� � hw02-library-helm\hw02-library-app\Chart.yaml  
```
dependencies:
    - name: postgresql
      version: 10.0.0
      repository: https://charts.bitnami.com/bitnami
      condition: postgresql.enabled
      tags:
        - hw02-postgres
```

**Note:** version - ��� ������ �����, � �� ����������.  


������������� ��������� �����������. ��� ������������ � ���������� charts/
```helm dependency update ./hw02-library-app```

## �������� ��������� � templates

### .\2020-12-otus-software-architect-lyulin\hw02-Kubernetes\kubernetes\hw02-library-app\values.yaml 

������� ��������� ����� ����������� �� Docker hub:  
```
image:
  repository: vlyulin/hw02-library-app
```
����������� ������ ��� ����������:
```
service:
  type: NodePort
  port: 80
  externalPort: 8080
  targetPort: http
```
����������� ingress:
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
***Note:*** path: - ��� ��� ����� ������� � ��������, � "nginx.ingress.kubernetes.io/rewrite-target: /hw02-Kubernetes/$2" - ��� ����, ������� ����� ������ �� ������� hw02-library-app-service

***Note:*** "path: /hw02-Kubernetes/()(.*)" ����������� ��-�� ����, ��� ��� �������� �� ������� � ���������� ������������ ���� http://arch.homework/hw02-Kubernetes/hw02-Kubernetes/...

��������� ��� ����������:
```
externalPostgresql:
  postgresqlDriver: org.postgresql.Driver
  postgresqlUsername: postgresadmin
  postgresqlPassword: pswd
  postgresqlDatabase: postgresdb
  postgresqlHost: "postgres"
  postgresqlPort: "5432"
```
� ��������� ��� subchart postgresql:
```
postgresql:
  enabled: true
  postgresqlUsername: postgresadmin
  postgresqlPassword: pswd
  postgresqlDatabase: postgresdb
  service:
    port: "5432"
```
***Note:*** ��������, ���� �� ���������� �� ����������.

### ..\2020-12-otus-software-architect-lyulin\hw02-Kubernetes\kubernetes\hw02-library-app\config.yaml 

������� ���� � �������� �������:
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

� spec ... template ... spec �������� ��������, ����� ���������� pod � ����� postgress

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
***Note:*** ��������� ������ ������� {{ postgresql.fullname ...}} 

�������� ���������� ���������:
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

### ��������� ��������� templates
`helm install hw02-library-app ./hw02-library-app --dry-run`  
���  
`.\hw02-Kubernetes\kubernetes\hw02-library-helm\helmInstallHw02LibraryApp-dry-run.bat`

## ���������� ����������  
### �������� ���������� ���������

Get all releases  
`helm ls --all-namespaces`  OR   `helm ls -A`

Delete release  
```
helm uninstall release_name -n release_namespace
helm uninstall hw02-library-app -n hw02
```
### ����� ���������

***Note:*** namespace hw02 ������ ���� ������ ������������� ��� ��������� ����������, �� ���� ���-�� ������ �� ���, �� ������� �������  
```kubectl create namespace hw02```

�� ���������� .\hw02-Kubernetes\kubernetes
������� ��������� ����������:  
```
helm install --replace --namespace=hw02 hw02-library-app ./hw02-library-app  
���
.\helmInstallHw02LibraryApp.bat
```

���� ��� ��������� ��������� �������� ������:  
`Error: cannot re-use a name that is still in use`    
�� ������� ������  
`kubectl get secret --all-namespaces -l "owner=helm"`  
������� ���  
`kubectl delete secret sh.helm.release.v1.hw02-library-app.v1 -n hw02`  
� ��������� ��������� �����  
***Note:*** ������ ������ ���: https://github.com/helm/helm/issues/4174  

���������, ����� pod hw02-library-app-<some hash> ����� � ��������� READY  
����������� ��������:
`kubectl get po -n hw02`

## �������� ����������������� ����������

### ��� Win 
� etc/host �������� ������  
`127.0.0.1 arch.homework`

C������ forward �� Ingress
����� ingress-nginx-controller pod'y ��������:  
`kubectl get pods -A --namespace hw02 | grep ingress`
�������� ��� ���� pod'� � �������  
```
kubectl port-forward -n kube-system pod/<ingress nginx pod> 80:80`
������: 
kubectl port-forward -n kube-system pod/ingress-nginx-controller-558664778f-sdzs7 80:80
```

## URLs:
```
http://arch.homework/
http://arch.homework/health
http://arch.homework/vlyulin/health
```
### ������

user: Admin pswd: 12345678  
user: User01 pswd: 12345678  
user: User02 pswd: 12345678  

---
## ������� �� debug pods

������� pod � �����������  
`kubectl get pods --namespace hw02`
������ �� ���� �������  
`kubectl port-forward -n hw02 pod/<pod> 80:8080`
kubectl port-forward -n hw02 pod/hw02-library-app-598d987447-dpdqc 80:8080

kubectl port-forward -n hw02 service/hw02-library-app-postgresql 5432:5432

���������� ����  
kubectl -n hw02 logs pod/hw02-library-app-79cd7b96b8-cjxmn

kubectl describe pod

����������, ��� ��� ������:  
```
kubectl exec <POD-NAME> -c <CONTAINER-NAME> -- <COMMAND>
kubectl exec --namespace="hw02" hw02-library-app-79cd7b96b8-gccnr -- ls /
docker exec -it hw02-library-app-79cd7b96b8-gccnr /bin/bash
kubectl exec --stdin --tty hw02-library-app-79cd7b96b8-gccnr -n hw02 -- /bin/bash
```

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
helm uninstall hw02-library-app -n hw02
```
Examples:  
`helm uninstall hw02-library-app -n hw02`






---
## �������� ������
Subcharts
https://helm.sh/docs/chart_template_guide/subcharts_and_globals/

���������
https://helm.sh/docs/chart_template_guide/yaml_techniques/


�������� ��������� Postgres:
������� � ��������� .\kubernetes  
Charts: https://github.com/bitnami/charts/tree/master/bitnami/postgresql  
��������� postgres:  
```
helm repo add bitnami https://charts.bitnami.com/bitnami
helm install hw02-postgres -f values.yaml bitnami/postgresql --namespace hw02  
```
��� 
`HelmInstallPostgres.bat` 

� ����� ���-�� ���:
helm install hw02-postgres bitnami/postgresql --namespace hw02
