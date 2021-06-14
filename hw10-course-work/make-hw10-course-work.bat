call gradle :hw10-course-work:Auth:clean
call gradle :hw10-course-work:Auth:build -x test

call gradle :hw10-course-work:BillingService:billing-client:clean -x test
call gradle :hw10-course-work:BillingService:billing-client:build -x test

call gradle :hw10-course-work:BillingService:billing-server:clean
call gradle :hw10-course-work:BillingService:billing-server:build -x test

call gradle :hw10-course-work:NotificationService:notification-client:clean
call gradle :hw10-course-work:NotificationService:notification-client:build -x test

call gradle :hw10-course-work:NotificationService:notification-server:clean
call gradle :hw10-course-work:NotificationService:notification-server:build -x test

call gradle :hw10-course-work:OrderService:order-client:clean 
call gradle :hw10-course-work:OrderService:order-client:build -x test

call gradle :hw10-course-work:OrderService:order-server:clean 
call gradle :hw10-course-work:OrderService:order-server:build -x test
