To build a REST service which is able to process at least 10K requests per second

Functional Requirements:

1. REST service that handles at least 10,000 requests per second. 
The service should accept:
A mandatory query parameter: id
An optional query parameter: URL endpoint
The service should:
Return "ok" on successful processing.
Return "failed" in case of errors.
Additionally if the Id has already been processed, return "Duplicate request, ignoring."

Concurrent HashMap can be used to store and deduplicate Ids efficiently. 
But due to the extension 2 requirement, I have used Redis to see if the Id has already been processed.

2. Logger
   Logging Framework (SLF4J) is used to for logging

Non-Functional Requirements:

Extension 1: Instead of firing an HTTP GET request to the endpoint, fire a POST request.

In my implementation, I have an enum REQUEST_TYPE for either to use a POST or GET. By default request type is set to POST.

Extension 2: Make sure the id deduplication works also when the service is behind a Load Balancer and 2
instances of your application get the same id simultaneously. 

I have used Redis to handle Deduplication because it will ensure that requests with the same id are only counted once per minute.


Extension 3: Instead of writing the count of unique received ids to a log file, send the count of unique received
ids to a distributed streaming service of your choice.

I have chosen Kafka to stream unique request counts to a distributed service instead of logging the unique request count to a file.

unique-requests-topic is the Topic that is subscribed to by the Kafka producer service. Byd efault, requests run every 60 seconds
 
VerveControllerTest is JUnit test to test the success or failure of the accept request. If the same id is run twice, then the test testAcceptRequest_Success would fail. 

Docker-Containers:

CONTAINER ID   IMAGE                              COMMAND                  CREATED              STATUS              PORTS                                        NAMES
e8d134acd655   confluentinc/cp-kafka:latest       "/etc/confluent/dock…"   44 seconds ago       Up 44 seconds       0.0.0.0:9092->9092/tcp                       kafka
50cac5d76c72   confluentinc/cp-zookeeper:latest   "/etc/confluent/dock…"   About a minute ago   Up About a minute   2888/tcp, 0.0.0.0:2181->2181/tcp, 3888/tcp   zookeeper
a36b50f37789   redis                              "docker-entrypoint.s…"   11 minutes ago       Up 11 minutes       0.0.0.0:6379->6379/tcp                       verve-redis
advgitha@AdvGithas-MBP verve % docker exec -it verve-redis redis-cli

127.0.0.1:6379> PING
PONG





