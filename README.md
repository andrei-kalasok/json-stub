# Power of Attorney Service
This awesome web service provides REST API for accessing power of attorney information of a user
  - Power of attorney details such as grantee, grantor and account details (/power-of-attorneys/{id})
  - Details for card products authorized by the power of attorney (/debit-cards/{id} and /credit-cards/{id})
  - Account details (/accounts/{id})
  - Some developer might have made an error somewhere

# Exercise!
  - Build a REST API presenting aggregated information from different services
  - Only show data that a user is actually authorized for
  - Handle any server errors you might run into gracefully
  
# Requirements
  - Requirements of the code and functionality is up to the candidate
  - We suggest using Java 11, Spring-Boot & Maven, but using Kotlin or Gradle is also fine
  - Perform whatever validation seems necessary
  - Don't return inactive products or accounts
  - (Optional) Expose the API over HTTPS
 
# Tips
  - Because every candidate has different experience and background, the candidate should decide on how complex code they want to show us
  - If the assignment is unclear, do what you feel is best and focus on the code, not the exercise
  - We look at the quality and readability of code that has been delivered more than if the functionality matches our expectations
  - Impress us!

# Solution

## General information and main idea

The examples from testdata/__files folder were taken as initial data for the application Database. 
During import the migrator builds authorization map based on following assumptions:
* Only an account owner can see the account  
* A card holder can see the card
* A power of attorney details is visible for both grantor and grantee
* Cards involved in power of attorney object are visible for both grantor and grantee 
* Only authorized users can access information from database

## Tech Stack description

### Maven

Build tool for building/testing/running project

**To build:** use `mvn package`
**To build and run tests:** use `mvn package`

### Spring boot

Basic framework to build stand-alone application.
Additional modules:
* Spring Data-JPA (Persistence see nl.rabobank.powerofattorney.akalasok.persistence) 
* Spring Data-Rest (Rest API see nl.rabobank.powerofattorney.akalasok.controller)
* Spring Security (Security layer see nl.rabobank.powerofattorney.akalasok.config.WebSecurityConfig and nl.rabobank.powerofattorney.akalasok.config.WebSecurityConfig)
* Spring Thymeleaf integration (Templating engine for web part see web/src/main/resources/templates)

### MySql

The solution uses MySql database for persistence layer. Usename/password, schema and url can be modified in application.properties

### Flyway

Flyway is used for the initial DB migration:
* Initial db schema (see common/resources/db/migration/V1__Initial_schema.sql)
* Import initial data from common/src/main/resources/testdata/__files (see db/migration/V2__Initial_data.java)

### Docker 

Docker is being used to start MySql DB local:
**To start Mysql in docker:** `docker-compose up -d`

Application data is persisted in mounted folder, `attorney-data` folder by default (can be modified in docker-compose.yml) 

**Note**: There might be permission error: 
```
ERROR: for attorney_mysql  Cannot start service mysql: b'Mounts denied: \r\nThe path <path-to-repo>/attorney-data\r\nis not shared from OS X and is not known to Docker.\r\nYou can configure shared paths from Docker -> Preferences... -> File Sharing.\r\nSee https://docs.docker.com/docker-for-mac/osxfs/#namespaces for more info.\r\n.'
```
The solution is to add persistence folder to permitted folders list in Docker Preferences->File sharing

### Other 

lombok - automatic generation of getters, setters, equals, hashCode and toString
modelmapper - automatic dto->entity/entity->dto mapping

## Project structure

### Common

Common module contains sharable Entity/Dto/Domain models, mappers, flyway migrations and authentication utilities

### Service

Rest API service (mail application) implementation of specified above Rest API

**To run:**
```
docker-compose up -d
cd service && mvn spring-boot:run
```

**Application runs on:** http://localhost:8080

Query examples:
```
curl http://localhost:8080/accounts/987654321 -u Super\ duper\ company:password1
curl http://localhost:8080/debit-cards/2222 -u Super\ duper\ company:password1
curl http://localhost:8080/credit-cards/3333 -u Super\ duper\ company:password1
curl http://localhost:8080/power-of-attorneys -u Super\ duper\ company:password1
curl http://localhost:8080/power-of-attorneys/1 -u Boromir:password5
```

HTTP error list:
* 401 - user is not authorized (no/bad credentials)
* 403 - user doesn't have access to requested data (see assumption below)
* 404 - requested data / url is not found  

### Web

Web application based on Spring + Thymeleaf. Auxiliary application to explore imported database (see common/src/main/resources/testdata/__files/users for users credentials)

**To run:**
```
docker-compose up -d
cd web && mvn spring-boot:run
```

**Application runs on:** http://localhost:8090

## Data migration

Data migration happens during first run of either web or service module  
