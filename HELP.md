# Read Me First
The following was discovered as part of building this project:

* The original package name 'com.example.spring-gql' is invalid and this project uses 'com.example.springgql' instead.

# Getting Started

### Reference Documentation
For further reference, please consider the following sections:

* [Official Apache Maven documentation](https://maven.apache.org/guides/index.html)
* [Spring Boot Maven Plugin Reference Guide](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/)
* [Create an OCI image](https://docs.spring.io/spring-boot/docs/2.7.2/maven-plugin/reference/html/#build-image)
* [Spring Web](https://docs.spring.io/spring-boot/docs/2.7.2/reference/htmlsingle/#web)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)

### Produced JSON when logging
```{"@timestamp":"2022-12-11T01:25:29.239+07:00","@version":"1","message":"{\"event\":\"GraphQL\",\"traceId\":\"6654d4861a962864\",\"spanId\":\"fd5c4ef85010e45a\",\"startTime\":1670696728946,\"finishTime\":null,\"elapsedTime\":287,\"additionalProperties\":null,\"httpStatus\":200,\"username\":null,\"url\":\"/graphql\",\"clientType\":\"PostmanRuntime/7.28.4\",\"requestHeader\":null,\"requestBody\":\"query { artists { id name } }\",\"httpMethod\":\"POST\",\"errorMessage\":null}","logger_name":"com.example.springgql.logging.LoggingService","thread_name":"http-nio-8080-exec-2","level":"INFO","level_value":20000,"traceId":"6654d4861a962864","spanId":"6654d4861a962864"}```

### Jacoco
```mvn clean verify``` and test report will auto generated

### Deploy
1. delete target to rebuild
   ```rm -rf target```
2. build jar file witch command ```./mvnw package``` or ```./mvnw package -DskipTests```
3. change docker repository to minikube ??~~~~
   ```minikube docker-env && eval $(minikube -p minikube docker-env)```
4. apply configmap
   ```kubectl apply -f config-maps.yaml```
5. deploy
   ```kubectl apply -f deployment.yaml && kubectl apply -f service.yaml```
6. get exposes url 
   ```minikube service --all```
7. enabling metrics & ingress (if not, created ingress wont getting address to be registered in etc/host)
   ```minikube addons enable metrics-server && minikube addons enable ingress```
8. shutdown
   ```kubectl delete service spring-gql-service && kubectl delete deploy spring-gql-deployment```
9. radicaly redeploy :)
   ```say deleting deployment && kubectl delete deploy spring-gql-deployment && say deleting service && kubectl delete service spring-gql-service && say deleting target && rm -rf target && say build image && ./mvnw package && docker build -t spring-gql:0.0.2-SNAPSHOT . && say deploying to kube && kubectl apply -f ./deployment/deployment-service.yaml```


### DEPLOY TO k8
* gcloud container clusters create ridho-portofolio-cluster \\n --num-nodes=2\\n --machine-type n1-standard-1\\n --zone asia-southeast2 --disk-size 20GB
* gcloud artifacts repositories create ridho-portofolio-repo     --repository-format=docker --location=asia-southeast2
* export GOOGLE_CLOUD_PROJECT=`gcloud config list --format="value(core.project)"`
* ./mvnw -DskipTests com.google.cloud.tools:jib-maven-plugin:build -Dimage=asia-southeast2-docker.pkg.dev/${GOOGLE_CLOUD_PROJECT}/ridho-portofolio-repo/spring-gql:v1
* gcloud auth configure-docker asia-southeast2-docker.pkg.dev
* ./mvnw -DskipTests com.google.cloud.tools:jib-maven-plugin:build -Dimage=asia-southeast2-docker.pkg.dev/${GOOGLE_CLOUD_PROJECT}/ridho-portofolio-repo/spring-gql:v1
* k config set-context --current --namespace=app