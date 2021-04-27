# ����:

## �������
��� ������� Auth ������� �� ������ ����� ���� ������� ��������� ���� � VM Options � ������� �������.  
��������:  
```
-Dserver.port=8082
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
gradlew :hw08-Idempotency:Auth:build
���
gradle :hw08-Idempotency:Auth:build
```

��������� ������ ����������
```
java -DDB_DRIVER=org.postgresql.Driver -DDB_URL=jdbc:postgresql://localhost:5432/postgres -DDB_USERNAME=postgresadmin -DDB_PASSWORD=pswd -jar ./hw08-Idempotency/Auth/build/libs/Auth-1.0.0.jar
```

## ������� docker-����� � �����������

�� ���������� ������� 2020-12-otus-software-architect-lyulin ��������� �������:  
```
docker build -f ./hw08-Idempotency/Auth/Dockerfile.hw08-Idempotency-auth --build-arg JAR_FILE=./hw08-Idempotency/Auth/build/libs/*.jar -t vlyulin/hw08-idempotency-auth .
```  
***None***: -t vlyulin/hw08-Idempotency-auth ����� ��������� tag  

### ��������� ������ ���������� �� docker
```
docker run -p 8080:8080 -t vlyulin/hw08-idempotency-auth
```
## ���������� ����� � DockerHub
https://hub.docker.com/

```
docker images
docker login
docker push vlyulin/hw08-idempotency-auth
```

## ������� Helm chart ��� ���������� hw02-library-app
mkdir .\hw08-Idempotency\Auth\kubernates\
cd .\hw08-Idempotency\Auth\kubernates\
helm create hw08-Idempotency-auth

### ������ ��������� � helm templates
- .\hw08-Idempotency\Auth\kubernates\hw08-Idempotency-auth\values.yaml  
- .\hw08-Idempotency\Auth\kubernates\hw08-Idempotency-auth\templates\config.yaml  
- .\hw08-Idempotency-auth\templates\deployment.yaml  
- .\hw08-Idempotency\Auth\kubernates\hw08-Idempotency-auth\templates\_helpers.tpl  
���������:  
```
{{- define "postgresql.fullname" -}}
{{- printf "%s-%s" .Release.Name "postgresql" | trunc 63 -}}
{{- end -}}
```

### ��������� ��������� templates
```
helm install hw08-idempotency-auth .\hw08-Idempotency\Auth\kubernates\hw08-idempotency-auth --dry-run
```
## ������� ���������� ��������� 
```
helm uninstall hw08-idempotency-auth
```

## ���������� ����������
```
helm install --replace hw08-idempotency-auth .\hw08-Idempotency\Auth\kubernates\hw08-idempotency-auth
```

## ������ 
### ����������� ��������� ����� �������
***�� ������***: https://medium.com/kubernetes-tutorials/kubernetes-dns-for-services-and-pods-664804211501
kubectl exec -it hw08-Idempotency-auth-6b5f58fb87-jtrct -- sh
PING hw08-Idempotency-auth.default.svc.cluster.local (10.97.121.80): 56 data bytes

***Note***
��� ��������� debug ����������
https://kubernetes.io/docs/tasks/debug-application-cluster/debug-running-pod/
