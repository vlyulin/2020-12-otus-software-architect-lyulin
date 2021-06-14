
helm uninstall order-service
helm install order-service .\OrderService\kubernates\order-service

helm uninstall auth-service
helm install auth-service .\Auth\kubernates\auth-service

helm uninstall billing-service
helm install billing-service .\BillingService\kubernates\billing-service

helm uninstall notification-service
helm install notification-service .\NotificationService\kubernates\notification-service

