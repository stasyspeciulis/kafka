Technical task
=========================

Application consists of two microservices:

    1. Dataloader - reads data from local directory and loads into Kafka topics, scheduled to run every 24 hours
    2. REST service - lets request customer by id, top N, customers/products count

Technology
================

    Java 17
    SpringBoot 2.6.6
    Kafka, ksqldb (tested on confluent-7.1.0)
    ksqldb-api-client 7.1.0

Services
================

    1. http://localhost:8902/dataloader - manualy run Dataloader from REST interface (Dataloader runs also as Spring scheduled task)
    2. http://localhost:8902/customer?id=6003 - get customer by id
    3. http://localhost:8902/customer/top?n=3 - get top N last registered customers
    4. http://localhost:8902/customer/count - get number of customers
    5. http://localhost:8902/product/count - get number of products
    
Details
================

README.docx    
