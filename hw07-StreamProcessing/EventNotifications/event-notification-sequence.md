sequenceDiagram
    participant C as Customer	
    participant RS as Registration/Authentification service
    participant MB as Message Broker
    participant OS as Ordering service
    participant BS as Billing service
    participant NS as Notification service

    C ->> RS: POST /auth/register
    Activate RS
    Note right of RS: Create user
    RS ->> MB: publish
    Activate MB
    RS -->> C: 201 CREATED
    Deactivate RS
    MB -->>+ BS: consume 
    Activate BS
    Deactivate MB

    Note right of BS: Create account
    Deactivate BS
    
    C ->>+ BS: POST /accounts/{accoint_id}/dopositation
    Note right of BS: Enter money
    BS -->>- C: 200 Ok 
    
    C ->> OS: POST /orders
	activate OS
    Note right of OS: Confirm an order
    OS ->> MB: publish 
    deactivate OS   
    Activate MB
    MB -->> BS: consume
    Activate BS
    Deactivate MB
    
    
    BS ->> OS: GET /orders/{order_id}
    activate OS
    Note right of BS: Withdraw money
    OS -->> BS: 200 OK
    deactivate OS
    
    alt is enough money
        BS ->> MB: publish event Happy message
        activate MB
    else is not enough money
        BS ->> MB: publish event Upset message
    end
    MB -->> NS: consume    
    deactivate MB
    Deactivate BS

    activate NS
    par Get user info        
        NS ->> RS: GET /users/{user_id}
        activate RS
        RS -->> NS: User info
        deactivate RS
    and Get Order info
        NS ->> OS: GET /users/{user_id}
        activate OS
        OS -->> NS: Order info
        deactivate OS
    end
    NS -> NS: msg
	deactivate NS

