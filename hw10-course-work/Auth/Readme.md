# ����:

## �������
��� ������� Auth ������� �� ������ ����� ���� ������� ��������� ���� � VM Options � ������� �������.  
��������:  
```
-Dserver.port=8085
```
![Server.port](./imgs/VMOptions.png)

��� ���������� � ����� ������ � ������ ������� ���� ���������� ���������� ��������� � ���� "Environment Variables":  
```
DB_DRIVER=org.postgresql.Driver;DB_URL=jdbc:postgresql://localhost:5432/postgres;DB_USERNAME=postgres;DB_PASSWORD=pswd
```

![Environment Variables](./imgs/EnvironmentVariables.png)

## ������� ����������
�� ���������� ������� 2020-12-otus-software-architect-lyulin
��������� �������: 

```
gradlew :hw10-course-work:Auth:build
���
gradle :hw10-course-work:Auth:build
```

��������� ������ ����������
```
java -DDB_DRIVER=org.postgresql.Driver -DDB_URL=jdbc:postgresql://localhost:5432/postgres -DDB_USERNAME=postgresadmin -DDB_PASSWORD=pswd -jar ./hw10-course-work/Auth/build/libs/Auth-1.0.0.jar
```

## ������� docker-����� � �����������

�� ���������� ������� 2020-12-otus-software-architect-lyulin ��������� �������:  
```
docker build -f ./hw10-course-work/Auth/Dockerfile.hw10-course-work.auth-service --build-arg JAR_FILE=./hw10-course-work/Auth/build/libs/*.jar -t vlyulin/auth-service .
```  
��� 
```
docker build -f ./Dockerfile.hw10-course-work.auth-service --build-arg JAR_FILE=./build/libs/*.jar -t vlyulin/auth-service .
```  
***None***: -t vlyulin/auth-service ����� ��������� tag  

### ��������� ������ ���������� �� docker
```
docker run -p 8080:8080 -t vlyulin/auth-service
```
## ���������� ����� � DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/auth-service
```

## ������� Helm chart ��� ���������� hw02-library-app
mkdir .\hw10-course-work\Auth\kubernates\
cd .\hw10-course-work\Auth\kubernates\
helm create auth-service

### ������ ��������� � helm templates
- .\hw10-course-work\Auth\kubernates\auth-service\values.yaml  
- .\hw10-course-work\Auth\kubernates\auth-service\templates\config.yaml  
- .\hw10-course-work-auth\templates\deployment.yaml  
- .\hw10-course-work\Auth\kubernates\auth-service\templates\_helpers.tpl  
���������:  
```
{{- define "postgresql.fullname" -}}
{{- printf "%s-%s" .Release.Name "postgresql" | trunc 63 -}}
{{- end -}}
```

### ��������� ��������� templates
```
helm install auth-service .\hw10-course-work\Auth\kubernates\auth-service --dry-run
```
## ������� ���������� ��������� 
```
helm uninstall auth-service
```

## ���������� ����������
```
helm install auth-service .\hw10-course-work\Auth\kubernates\auth-service
```

## ������ 
### ����������� ��������� ����� �������
***�� ������***: https://medium.com/kubernetes-tutorials/kubernetes-dns-for-services-and-pods-664804211501
kubectl exec -it hw10-course-work-auth-6b5f58fb87-jtrct -- sh
PING hw10-course-work-auth.default.svc.cluster.local (10.97.121.80): 56 data bytes

***Note***
��� ��������� debug ����������
https://kubernetes.io/docs/tasks/debug-application-cluster/debug-running-pod/
