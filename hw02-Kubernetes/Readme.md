# Steps:

## Verifying Application Build

From the 2020-12-otus-software-architect-lyulin project directory, execute the command:

```gradle :hw02-Kubernetes:build --no-daemon```
or
build-hw02-kubernates.bat

## Building a docker file with an application

From the 2020-12-otus-software-architect-lyulin project directory, execute the command:
`Docker image build --rm --file ./hw02-Kubernetes/Dockerfile.hw02-Kubernetes -t hw02-library-app .`  
or  
`make-image-hw02-library-app.bat` 

## Post the image to DockerHub https://hub.docker.com/

```
docker images
docker tag <Image ID для hw1-health-app> vlyulin/hw02-library-app:latest
docker tag ca009361582a vlyulin/hw02-library-app:latest
docker login
docker push vlyulin/hw02-library-app
```
The vlyulin / hw02-library-app repository will appear on DockerHub

Made according to instructions: https://dker.ru/docs/docker-engine/get-started-with-docker/tag-push-pull-your-image/

Public view: https://hub.docker.com/repository/docker/vlyulin/hw02-library-app

## Start minikube
`minikube start`

## Add Ingress
`minikube addons enable ingress`
Verify the ingress installation with the command:
`kubectl get pods -A | grep ingress`

## Install Helm

Helm installation instructions:
https://github.com/helm/helm/releases/latest

### Add the bitnami repository where the official charts are stored
helm repo add bitnami https://charts.bitnami.com/bitnami

## Create a chart for the hw02-library-app
cd ./hw02-Kubernetes/kubernetes
helm create hw02-library-app

### Add postgresql dependency to the chart. 

Changes are made to hw02-library-helm\hw02-library-app\Chart.yaml  
```
dependencies:
    - name: postgresql
      version: 10.0.0
      repository: https://charts.bitnami.com/bitnami
      condition: postgresql.enabled
      tags:
        - hw02-postgres
```

**Note:** version - this is the version of the chart, not the app.  


Install the specified dependencies. They are added to the directory charts/
```helm dependency update ./hw02-library-app```

## Making changes to templates

### .\2020-12-otus-software-architect-lyulin\hw02-Kubernetes\kubernetes\hw02-library-app\values.yaml 

Specify the built image hosted on the Docker hub:  
```
image:
  repository: vlyulin/hw02-library-app
```
Setting up a service for the application:
```
service:
  type: NodePort
  port: 80
  externalPort: 8080
  targetPort: http
```
Setting up ingress:
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
***Note:*** path: - this is what will be indicated in the browser, and "nginx.ingress.kubernetes.io/rewrite-target: / hw02-Kubernetes / $ 2" is the path that will be called on the hw02-library-app-service

***Note:*** "path: /hw02-Kubernetes/()(.*)" was needed because the path http: //arch.homework/hw02-Kubernetes/hw02-Kubernetes / ...

Application parameters:
```
externalPostgresql:
  postgresqlDriver: org.postgresql.Driver
  postgresqlUsername: postgresadmin
  postgresqlPassword: pswd
  postgresqlDatabase: postgresdb
  postgresqlHost: "postgres"
  postgresqlPort: "5432"
```
And the parameters for the postgresql subchart:
```
postgresql:
  enabled: true
  postgresqlUsername: postgresadmin
  postgresqlPassword: pswd
  postgresqlDatabase: postgresdb
  service:
    port: "5432"
```
***Note:*** Perhaps it would be more correct to combine them.

### ..\2020-12-otus-software-architect-lyulin\hw02-Kubernetes\kubernetes\hw02-library-app\config.yaml 

Create config.yaml file and add secrets:
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

In spec ... template ... spec add wait for pod with base postgress to rise

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
***Note:***  

Add environment variables:
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

### Check the generation of templates
`helm install hw02-library-app ./hw02-library-app --dry-run`  
or  
`.\hw02-Kubernetes\kubernetes\hw02-library-helm\helmInstallHw02LibraryApp-dry-run.bat`

## Install the application  
### Removing a previous installation

Get all releases  
`helm ls --all-namespaces`  OR   `helm ls -A`

Delete release  
```
helm uninstall release_name -n release_namespace
helm uninstall hw02-library-app -n hw02
```
### New installation

***Note:*** namespace hw02 should be created automatically when installing the application, but if something goes wrong, then create it manually
```kubectl create namespace hw02```

From the directory. \ Hw02-Kubernetes \ kubernetes
Application installation command:  
```
helm install --replace --namespace=hw02 hw02-library-app ./hw02-library-app  
или
.\helmInstallHw02LibraryApp.bat
```

If we get an error when reinstalling:
`Error: cannot re-use a name that is still in use`
then we find the secret
`kubectl get secret --all-namespaces -l" owner = helm "`
delete it
`kubectl delete secret sh.helm.release.v1.hw02-library-app.v1 -n hw02`
and run the installation again
*** Note: *** Recipe found here: https://github.com/helm/helm/issues/4174

Wait until pod hw02-library-app- <some hash> is READY
Checked by the command:
`kubectl get po -n hw02`

## Checking the functionality of the application

### Under Win
Add the line to etc / host
`127.0.0.1 arch.homework`

Make a forward on Ingress
Find ingress-nginx-controller pod'y with the command:
`kubectl get pods -A --namespace hw02 | grep ingress`
Insert the name of this pod into the command
``,
kubectl port-forward -n kube-system pod / ingress nginx pod 80:80
``,
example:
kubectl port-forward -n kube-system pod / ingress-nginx-controller-558664778f-sdzs7 80:80

## URLs:
``,
http: //arch.homework/
http: //arch.homework/health
http: //arch.homework/otusapp/vlyulin/health
http: //arch.homework/metrics
``,
### Logins

user: Admin pswd: 12345678
user: User01 pswd: 12345678
user: User02 pswd: 12345678

---
## Notes on debug pods

Find a pod with an application
`kubectl get pods --namespace hw02`
We make a forward on him
`kubectl port-forward -n hw02 pod / <pod> 80: 8080`
kubectl port-forward -n hw02 pod / hw02-library-app-598d987447-dpdqc 80: 8080

kubectl port-forward -n hw02 service / hw02-library-app-postgresql 5432: 5432

View logs
kubectl -n hw02 logs pod / hw02-library-app-79cd7b96b8-cjxmn

kubectl describe pod

See what's inside:
``,
kubectl exec <POD-NAME> -c <CONTAINER-NAME> - <COMMAND>
kubectl exec --namespace = "hw02" hw02-library-app-79cd7b96b8-gccnr - ls /
docker exec -it hw02-library-app-79cd7b96b8-gccnr / bin / bash
kubectl exec --stdin --tty hw02-library-app-79cd7b96b8-gccnr -n hw02 - / bin / bash
``,

## Deleting a release
#### Get all releases
``,
helm ls --all-namespaces
OR
helm ls -A
``,
#### Delete release
``,
helm uninstall release_name -n release_namespace
helm uninstall hw02-library-app -n hw02
``,
Examples:
`helm uninstall hw02-library-app -n hw02`
