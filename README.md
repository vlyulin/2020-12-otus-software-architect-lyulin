<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4" />

# ���� "Software Architect" � OTUS

# ����������:
* [�������](#�������)
* [������ hw01-Kubernetes](#������-hw01-Kubernetes)

# �������
��� ���������: ����� ����� ����������
�������� �����: 
������: 2020-12

## ������ hw01-Kubernetes<a name="������-hw01-Kubernetes"></a>
������ ������ � Kubernetes (����� 2)
������� ����������� ������, �������
1) �������� �� ����� 8000
2) ����� http-�����
GET /health/
RESPONSE: {"status": "OK"}

C������ �������� ����� ���������� � �����.
�������� ����� � dockerhub

�������� ��������� ��� ������ � k8s ��� ����� �������.

��������� ������ ��������� �������� Deployment, Service, Ingress.
� Deployment ����� ���� ������� Liveness, Readiness �����.
���������� ������ ������ ���� �� ������ 2. Image ���������� ������ ���� ������ � Dockerhub.

� Ingress-� ������ ���� �������, ������� ��������� ��� ������� � /otusapp/{student name}/* �� ������ � rewrite-�� ����. ��� {student name} - ��� ��� ��������.

���� � �������� ������ ���� arch.homework. � ����� ����� ���������� ���������� GET ������ �� http://arch.homework/otusapp/{student name}/health ������ �������� {�status�: �OK�}.

�� ������ ������������
0) ������ �� github c �����������. ��������� ������ ������ � ����� ����������, ��� ����� ����� ���� �� ��� ��������� ����� �������� kubectl apply -f .
1) url, �� �������� ����� ����� �������� ����� �� ������� (���� ���� � postman�). 
