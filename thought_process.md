To build a Java REST service capable of processing 10,000 requests per second, while satisfying the outlined requirements and extensions, we can implement a solution using the following technologies:

Spring Boot: To easily create the REST service with embedded Tomcat and make handling HTTP requests simple.
Concurrent HashMap: To store and deduplicate IDs efficiently.
Scheduled ExecutorService: To log the count of unique requests per minute.
HttpClient (from java.net.http): To make HTTP requests.
Logging Framework (SLF4J + Logback): To log information to a file.
Kafka (for Extension 3): To stream unique request counts to a distributed service


CONTAINER ID   IMAGE                              COMMAND                  CREATED              STATUS              PORTS                                        NAMES
e8d134acd655   confluentinc/cp-kafka:latest       "/etc/confluent/dock…"   44 seconds ago       Up 44 seconds       0.0.0.0:9092->9092/tcp                       kafka
50cac5d76c72   confluentinc/cp-zookeeper:latest   "/etc/confluent/dock…"   About a minute ago   Up About a minute   2888/tcp, 0.0.0.0:2181->2181/tcp, 3888/tcp   zookeeper
a36b50f37789   redis                              "docker-entrypoint.s…"   11 minutes ago       Up 11 minutes       0.0.0.0:6379->6379/tcp                       verve-redis
advgitha@AdvGithas-MBP verve % docker exec -it verve-redis redis-cli

127.0.0.1:6379> PING
PONG

