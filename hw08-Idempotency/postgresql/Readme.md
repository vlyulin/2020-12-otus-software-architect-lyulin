
## Установить postgress
```
helm install hw08-postgres -f values.yaml bitnami/postgresql
```

## Port forward
```
kubectl port-forward --namespace default svc/hw08-postgres-postgresql 5432:5432
```

## Stop vmmem
In Powershell
```
wsl --shutdown
``` 

cd %UserProfile%


