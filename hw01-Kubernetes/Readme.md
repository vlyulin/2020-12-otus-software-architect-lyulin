# Steps:

## Verifying Application Build

From the 2020-12-otus-software-architect-lyulin project, execute the command:

`gradle :hw01-Kubernetes:build --no-daemon`  
or  
`build-hw1-kubernates.bat` 

## Building a docker file with an application

From the 2020-12-otus-software-architect-lyulin project, execute the command:
`Docker image build --rm --file ./hw01-Kubernetes/Dockerfile.hw01-Kubernetes -t hw1-health-app .`
or  
`make-image-hw1-health-app.bat` 

## Checking the health of the created image hw1-health-app

Execute command: `docker run --publish 8080:8080 hw1-health-app` 
or from the hw01-Kubernetes directory execute `run_hw1-health-app.bat`

In a browser: 
http://localhost:8080/hw01-Kubernetes/health
http://localhost:8080/hw01-Kubernetes/sometext

## Post the image to DockerHub

```docker images
docker tag <Image ID для hw1-health-app> vlyulin/hw1-health-app:latest
docker login
docker push vlyulin/hw1-health-app
```
The vlyulin / hw1-health-app repository will appear on DockerHub

Made according to instructions: https://dker.ru/docs/docker-engine/get-started-with-docker/tag-push-pull-your-image/

Public view: https://hub.docker.com/r/vlyulin/hw1-health-app

## Installing minikubes

> Instructions: https://kubernetes.io/ru/docs/setup/learning-environment/minikube/#%D1%83%D0%BA%D0%B0%D0%B7%D0%B0%D0%BD%D0%B8%D0%B5-%D0%B4%D1%80%D0%B0%D0%B9%D0%B2%D0%B5%D1%80%D0%B0-%D0%B2%D0%B8%D1%80%D1%82%D1%83%D0%B0%D0%BB%D1%8C%D0%BD%D0%BE%D0%B9-%D0%BC%D0%B0%D1%88%D0%B8%D0%BD%D1%8B
1. Download minikubes from the link https://github.com/kubernetes/minikube/releases/latest/download/minikube-installer.exe
2. Install: minikube start --vm-driver = docker
3. Verifying the installation

    `minikube status`
    
## Kubernates

## Adding Ingress

minikube addons enable ingress

Check installation

minikube kubectl get pods -A | grep ingress
 
minikube kubectl get svc -A | grep ingress

### application deployment

```minikube kubectl -- apply -f .\hw1-health-app-deployment.yaml -f .\hw1-health-app-service.yaml -f .\hw1-health-app-ingress.yaml```

### Functional check

```minikube kubectl -- port-forward service/hw1-health-app-service 80:8000```

Without stopping its work in the browser, enter
http://localhost/hw01-Kubernetes/health

### Checking the ingress

Applies to windows

Find ingress-nginx-controller pod'y with the command:
`kubectl get pods -A | grep ingress`

Insert the name of this pod into the command  
`kubectl port-forward -n kube-system pod/<ingress nginx pod> 80:80`

For example:   
`kubectl port-forward -n kube-system pod/ingress-nginx-controller-558664778f-b4k8s 80:80`

Note: the process remains hanging

Add the line to etc/host  
`127.0.0.1 arch.homework`

Checking:  
`curl -v http://arch.homework/otusapp/vlyulin/health`  
or  
`http://arch.homework/health`
