<?xml version="1.0" encoding="UTF-8"?>
<module type="JAVA_MODULE" version="4" />

# Course "Microservices Architect", OTUS
[Russian](README.ru.md)

# Content:
* [Student](#Student)
* [Module hw01-Kubernetes](#Module-hw01-Kubernetes)
* [Module hw02-Kubernetes](#Module-hw02-Kubernetes)
* [Module hw03-Prometheus](#Module-hw03-Prometheus)
* [Module hw05-Apigateway](#Module-hw05-Apigateway)
* [Module hw07-StreamProcessing](#Module-hw07-StreamProcessing)
* [Module hw08-Idempotency](#Module-hw08-Idempotency)
* [Module hw09-Decomposition-patterns](#Module-hw09-Decomposition-patterns)
* [Module hw10-course-work](#Module-hw10-course-work)

# Student
Full name of the listener: Vadim Lyulin  
Course: Microservices Architect  
Group: 2020-12

## Module hw01-Kubernetes<a name="Module-hw01-Kubernetes"></a>
Kubernetes Basics (part 2)
Homework: Create a minimal service that
1) answers at the port 8000
2) has http-method
GET /health/
RESPONSE: {"status": "OK"}

Build a docker image of the application locally.
Push the image to dockerhub.

Write deployment manifests in k8s for this service.

Manifests should describe the Deployment, Service, Ingress entities.
In Deployment, Liveness, Readiness of the sample can be specified.
The number of replicas must be at least 2. The container image must be listed from Dockerhub.

The Ingress should have a rule that will forward all requests from / otusapp / {student name} / * to the service with a rewrite path. Where {student name} is the name of the student.

Ingress host should be arch.homework. As a result, after applying the GET manifests, the request for http: //arch.homework/otusapp/ {student name} / health should return {“status”: “OK”}.

Should be provided:
0) link to github with manifests. Manifests must be in the same directory so that you can apply them all with one command kubectl apply -f.
1) url, by which you can get a response from the service (or a test in postman).

## Module hw02-Kubernetes<a name="Module-hw02-Kubernetes"></a>

Homework: Infrastructure patterns  
Make the simplest RESTful CRUD to create, delete, view and update users.
API example - https://app.swaggerhub.com/apis/otus55/users/1.0.0

Add a database for the application.
Application configuration should be stored in Configmaps.
Database accesses should be stored in Secrets.
Initial migrations must be formalized as a Job, if required.
Ingress-s should also lead to the url arch.homework / (as in the previous task)

Should be provided:
1) link to the directory in github where the directory with Kubernetes manifests is located
2) instructions for launching the application.
- the command to install the database from helm, along with the values.yaml file.
- apply initial migrations command
- kubectl apply -f command, which runs Kubernetes manifests in the correct order
3) Postman collection, which will present examples of requests to the service to create, get, change and delete a user. Important: in the postman collection, use the base url - arch.homework.


Extra tasks:
+3 point for templating the application in helm 3 charts
+2 points for using the official helm chart for the database

## Module hw03-Prometheus<a name="Module-hw03-Prometheus"></a>

Homework: Prometheus. Grafana

Instrument the service from the previous task with metrics in the Prometheus format using a library for your framework and programming language.

Make a dashboard in Grafana, in which there would be metrics broken down by API methods:

    Latency (response time) with quantiles of 0.5, 0.95, 0.99, max
    RPS
    Error Rate - number of 500 responses

Add graphs with metrics for the whole service to the dashboard, taken from nginx-ingress-controller:

    Latency (response time) with quantiles of 0.5, 0.95, 0.99, max
    RPS
    Error Rate - number of 500 responses

Set up alert in Grafana for Error Rate and Latency.

Should be provided:: 0) screenshots of the dashboards with charts at the time of stress testing of the service. For example, after 5-10 minutes of exercise.

    json dashboards..

Extra tasks (+5 points) Using the existing system metrics from Kubernetes, add graphs with metrics to the dashboard:

    Application pod memory consumption
    Pod consumption of the CPU application

Instrument the database with the prometheus exporter for this database. Add graphs with database performance metrics to the common dashboard.

Alternative task for 1 point (if you don't want to put prometheus in minikube yourself) - https://www.katacoda.com/schetinnikov/scenarios/prometheus-client

## Module hw05-Apigateway<a name="Module-hw05-Apigateway"></a>

Homework: Backend for frontends. Apigateway

Add user authentication and registration to the application.

Implement the "Change and View Data in a Customer Profile" scenario. The user is registering. He goes under him and receives data about his profile at a certain URL. Can change the data in the profile. Profile data for reading and editing should not be available to other clients (authenticated or not).

Should be provided: 0) description of the architectural solution and the scheme of interaction of services (in the form of a picture)

    application installation command (from helm or from manifests). Be sure to indicate in which namespace you want to install. 1 *) api-gateway installation command, if different from nginx-ingress.
    postman tests that run the script:

    user registration 1
    checking that changing and getting user profile is not available without login
    user login 1
    change user profile 1
    checking that the profile has changed
    exit * (if exists)
    user registration 2
    user login 2
    checking that user 2 does not have access to read and edit the profile of user 1.

In tests it is necessary to have

    availability of {{baseUrl}} for url
    using domain arch.homework as initial value {{baseUrl}}
    using randomly generated data in a script
    displaying request data and response data when run from the command line with newman.

## Module hw07-StreamProcessing<a name="Module-hw07-StreamProcessing"></a>

Homework: Stream processing

Implement an ordering service. Billing service. Service of notifications.

When creating a user, you need to create an account in the billing service. The billing service should be able to put money on the account and withdraw money.

The notification service allows you to send a message by email. And allows you to get a list of messages by API method.

The user can create an order. The order has a parameter - the price of the order. The order takes place in 2 stages:

    first, we withdraw money from the user using the billing service
    we send the user an e-mail message with the results of ordering. If the billing has confirmed the payment, a letter of happiness should be sent. If not, then a letter of grief.

We simplify and believe that nothing bad can happen to services (they cannot fall, etc.). The service does not actually send notifications, but simply saves them to the database.

THEORETICAL PART (5 points): 0) Design the interaction of services when creating orders. Provide options for interactions in the following styles in the form of a sequence diagram describing the API in IDL:

    only HTTP communication
    event interaction with the use of a message broker for notifications (notifications)
    Event Collaboration communication style using a message broker
    the option that seems to you the most adequate for solving this problem. If it matches one of the options above, just check it.

PRACTICAL PART (5 points): Choose one of the options and implement it. The output should be 0) a description of the architectural solution and a diagram of the interaction of services (in the form of a picture)

    application installation command (from helm or from manifests). Be sure to indicate in which namespace you want to install.
    postman tests that run the script:

    Create user. A billing account must be created.
    Put money into the user's account through the billing service.
    Make an order for which there is enough money.
    View the money in the user's account and make sure that they have been withdrawn.
    View sent messages in the notification service and make sure that the message was sent
    Make an order for which there is not enough money.
    View the money in the user's account and make sure that their amount has not changed.
    View sent messages in the notification service and make sure that the message was sent.

Required in tests

    availability of {{baseUrl}} for url
    using domain arch.homework as initial value {{baseUrl}}
    displaying request data and response data when run from the command line with newman.

## Module hw08-Idempotency<a name="Module-hw08-Idempotency"></a>

Idempotency and API commutability in HTTP and queues

Create the service "Order" (or use the service from the last lesson) and make idempote for one of its methods, for example, "create order".

The output should be: 0) a description of what pattern was used to implement idempotency
* application installation command (from helm or from manifests). Be sure to indicate in which namespace you need to install and the namespace creation command, if it is important for the service.
* tests in postman

In tests it is necessary to have

* using domain arch.homework as initial value {{baseUrl}}

## Module hw09-Decomposition-patterns<a name="Module-hw09-Decomposition-patterns"></a>

Microservices decomposition patterns

Divide your application into multiple microservices for future changes.

Try to make several splits and try to evaluate them. Select the option that you will implement.

At the output, you must provide

* Custom scripts
* The general scheme of interaction of services.
* For each service, describe the purpose of the service and its area of responsibility.
* Describe the contracts for the interaction of services with each other.

## Module hw10-course-work<a name="Module-hw10-course-work"></a>

Distributed transactions

Implement a distributed transaction.
You can use the script below for an online store or create your own.

Default scenario: Implement the services "Payment", "Warehouse", "Delivery".

For the "Order" service, within the "create order" method, implement a distributed transaction mechanism (based on the Saga or two-phase commit).
When creating an order, you must:
* in the "Payment" service, make sure that the payment went through
* in the "Warehouse" service to reserve a specific product in the warehouse
* in the "Delivery" service, reserve a courier for a specific time slot.

If at least one of the items did not work out, you need to roll back all the other changes.

Should be provided:
* a description of what pattern was used to implement a distributed transaction
* application installation command (from helm or from manifests). Be sure to indicate in which namespace you need to install and the namespace creation command, if it is important for the service.
* tests in postman. In tests, it is mandatory to use the arch.homework domain as the initial value {{baseUrl}}
