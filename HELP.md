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