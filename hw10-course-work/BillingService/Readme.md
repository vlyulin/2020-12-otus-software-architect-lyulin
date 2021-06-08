# Billing Service

## ������� ����������

�� ���������� ������� 2020-12-otus-software-architect-lyulin ��������� �������: 

```
gradlew :hw10-course-work:BillingService:billing-server:build -x test
���
gradle hw10-course-work:BillingService:billing-server:build -x test
```

## ������� docker-����� � �����������

�� ���������� ������� 2020-12-otus-software-architect-lyulin ��������� �������:  
```
docker build -f ./hw10-course-work/BillingService/Dockerfile.hw10-cource-work.billing-service --build-arg JAR_FILE=./hw10-course-work/BillingService/billing-server/build/libs/*.jar -t vlyulin/billing-service .
```
��� �� ���������� ./hw10-course-work/BillingService
```
docker build -f Dockerfile.hw10-cource-work.billing-service --build-arg JAR_FILE=./billing-server/build/libs/*.jar -t vlyulin/billing-service .
```  
***None***: -t .. ����� ��������� tag  

## ���������� ����� � DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/billing-service
```

## ������� Helm chart ��� ���������� billing-service
mkdir .\hw10-course-work\BillingService\kubernates
cd .\hw10-course-work\BillingService\kubernates
helm create billing-service

### ��������� ��������� templates
```
helm install billing-service .\hw10-course-work\BillingService\kubernates\billing-service --dry-run
```
## ������� ���������� ��������� 
```
helm uninstall billing-service
```

## ���������� ����������
```
helm install billing-service .\hw10-course-work\BillingService\kubernates\billing-service
```


# Debug

kubectl exec --stdin --tty billing-service-7d87c6cdf9-9p78p -- /bin/sh

������ �����������  
docker ps  
����� � ���������  
docker exec -it e5fc62bc4eee /bin/bash

��������� ����������������� �������������� � kubernates ���������� ��� ingress
kubectl port-forward po/billing-service-7d87c6cdf9-hktvx 8080:8080

��������� ����������������� ������� billing-service ��� ingress
kubectl port-forward svc/billing-service 80:80

��������� ��������� �������
kubectl get service billing-service -o json
kubectl describe services billing-service

���� ��� ������� �� ������� � pod (����������) ���� ��������� ��������� � deployment.yaml 
������ ���� �� 
ports:
            - name: http
              containerPort: 80
              protocol: TCP

�
ports:
            - name: http
              containerPort: {{ .Values.service.externalPort }}
              protocol: TCP
