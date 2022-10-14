
## How To Run

1. Clone Project from Github
```
 git clone https://github.com/Nazel7/todo-app.git

```
2. Navigate to the project directory and follow the step below

### Build project And Get docker image
```
./mvnw clean install -DskipTests
copy docker image from log.....
docker with name:  docker.io/nazel7/td-todoservice:0.0.1-SNAPSHOT
```
### Run the Test cases
```
./mvnw test

```
### Run on Docker

 instantiate image container on port 8090
```
docker run -p 8090:8090 docker.io/nazel7/td-todoservice:0.0.1-SNAPSHOT
```

## URL to APis collections:
```
bank_base_url_d: http://localhost:8090/apis/todo-service/

postman_collection: https://www.getpostman.com/collections/7b4ca7f13d792828f546
```

# Writing
1. My Experience with core banking
```
I have extensive experience working in the Fintech companies and building Digigal banking solutions working in the bank.
I have worked in places like Mikro.africa, Stanbic IBTC bank, Mkobo Micro finance bank as well as investment company which directly and indirectly impacted many based on feedbacks. 
I have consulted for foreign companies like Pericius, India / Tecnotree, Finland building their SAAS application with high profile clients
using this solution.

In recent time, solutions I have built from scratch are not limited to the following;
1. Transaction service processing different kind of transactions with advance even-driven architecture
2. Web service Application for Inter-bank transaction processing
3. Hybrid BVN service for BVN verification.
4. SAAS SDk for Digital Contract management.

My current role span through vendor interfacing, training developers and managing processes on bankend engineering
```

5. A quick dive into the mini project architecture
```
1. Notable stack and library used are but not limited to: Java, Spring boot, Hibernate, Lombok, H2 Database,
 hibernate-validator, spring-boot actuator, log4j, Mockito and AssertJ for testing
2. Application decouply following SOLID principles and using alot of factory pattern for seperation of concern and also 
   builder pattern and lombok was also used for pojo enrichment with hibernate validation for schema enrichement i.e no traditional setter and getter 
   and bean management made easy.
3. final keyword was used where necessary for a little bit faster variable processing this is also for request encrichement.
5. Error first check strategy was used for faster operations; "why do I need to continue request velocity when the payload is invalid?"
6. Application enchrished with logging to aid in debugging but limited implimentation was ascertained as
   just for the scope of this mini project.
7. Unit and Integration test available for roburstness and proper application understanding and management.
8. the test required fetching list of todos without putting in place further consideration, I have built the API to accommodate
fetch by pagination for elasticity and proper API load managment. additional Optional keys to the API are 

RequestParam (Integer)
** pageNo
** size
** searchIndex (to search by status and date)
```




