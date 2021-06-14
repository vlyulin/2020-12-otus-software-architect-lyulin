
## Установить Zipkin сервер
kubectl apply -f .\zipkin-server-pod.yaml
kubectl apply -f .\zipkin-service.yaml

## Port forward
```
kubectl port-forward --namespace default svc/zipkin-service 9411:9411
```
