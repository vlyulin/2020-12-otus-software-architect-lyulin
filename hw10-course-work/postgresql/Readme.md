
## Установить postgress
```
helm install cw-postgres -f values.yaml bitnami/postgresql
```

## Port forward
```
kubectl port-forward --namespace default svc/cw-postgres-postgresql 5432:5432
```

## Stop vmmem
In Powershell
```
wsl --shutdown
``` 

cd %UserProfile%


