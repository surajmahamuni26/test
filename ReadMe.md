# Rewards Service

### Reference Documentation
Rewards Service is an application which holds Customer data along with the transaction history and returns reward points for transactions made by customer in a specific month or in total.

##### API Design
```[http://localhost:8080/api/v1/rewards?customerId={customerId}&monthOfPurchase={monthOfPurchase}]```

customerId : ID of the customer  
monthOfPurchase : Month of Year / "total"  

Acceptable values for monthOfPurchase attribute  
 * January, February, March, April, May, June,
   July, August, September, October, November, December
 * Total

##### Response Structure
```
{
    "customerId": 1,
    "rewardPoints": 17030.0
}
```

#### Gotchas
API could return with 404 status code in either of the following scenarios
 * customerId does NOT exist in the database
 * monthOfPurchase receives value outside of acceptable values list

### Project Specifications

##### Dependencies 
 * spring-boot-starter-web
 * spring-boot-starter-data-jpa
 * h2database

##### Build Tool
 * Gradle

##### Instructions to build and run the project
Build the project
```
./gradlew clean build
```

Run the project
```
./gradlew bootRun
```

### Test data
##### Data in Customer table
customerId | customerName 
--- | --- 
1 | Naresh
2 | Suresh
3 | Ramesh

##### Data in Transaction table
transactionId | transactionTime | transactionMonth | purchaseValue | customerId
--- | --- | --- | --- | ---
001 | 2022-08-26T05:47:08.644 | JANUARY | 1000 | 1
002 | 2022-08-26T05:47:08.644 | FEBRUARY | 10 | 1
003 | 2022-08-26T05:47:08.644 | MARCH | 7580 | 1
004 | 2022-08-26T05:47:08.644 | JANUARY | 10 | 2
005 | 2022-08-26T05:47:08.644 | FEBRUARY | 10 | 2
006 | 2022-08-26T05:47:08.644 | JANUARY | 300 | 3

Relationships between tables are created by @OneToMany and @ManyToOne bi-directional relationship directives in Spring Data JPA