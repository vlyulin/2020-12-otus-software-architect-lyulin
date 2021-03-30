sequenceDiagram
    participant C as Customer	
    participant RS as Registration/Authentification service
    participant OS as Ordering service
    participant BS as Billing service
    participant NS as Notification service

    C ->>+ RS: POST /auth/register
    Note right of RS: Create user
	RS ->>+ BS: POST /accounts
    Note right of BS: Create account
	BS ->>- RS: 201 CREATED
    RS -->>- C: 201 CREATED

    C ->>+ BS: POST /accounts/{accoint_id}/dopositation
    Note right of BS: Enter money
    BS -->>- C: 200 Ok 
    
    C ->> OS: POST /orders
	activate OS
    Note right of OS: Confirm an order
    OS ->> BS: POST /accounts/{account_id}/withdraw
	activate BS
    Note right of BS: Withdraw money
	activate NS
    alt is enough money
        BS -> NS: POST /send_email {template_id, email, context}
        Note right of NS: Happy msg
        NS -->> BS: 202 ACCEPTED
        BS -->> OS: 200 OK        
    else is not enough money
        BS -> NS: POST /send_email {template_id, email, context}
        Note right of NS: Upset msg
        NS -->> BS: 202 ACCEPTED
        BS -->> OS: 400 Insufficient funds to write off
    end
	deactivate BS
    alt is enough money
        OS -->> C: 200 OK Order has been precessed
    else is not enough money
        OS -->> C: 400 Insufficient funds to write off
    end
	deactivate OS
    NS -> NS: msg
	deactivate NS

