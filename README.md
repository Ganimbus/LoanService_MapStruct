# Project for the Loan Service of a Banking Institution

This project consists of a basic structure to manage the loans requested by clients of a banking institution.

## Table of Contents

- [Description](#description)
- [Features](#features)
- [Technologies Used](#technologies-used)
- [Project Configuration](#project-configuration)
- [Project Execution](#project-execution)
- [Usage](#usage)
- [Considerations](#considerations)

## Description

The purpose of this project is focused on learning various tools employed through the Spring Boot environment and the use of the Java language.

## Features

- Customer management.
- Request management.
- Loan management.
- Blacklist of customers who cannot access loans.

## Technologies Used

For all technologies, their most up-to-date versions as of December 2023 were employed.
- Use of [IntelliJ IDEA](https://www.jetbrains.com/idea/) IDE and [Visual Studio Code](https://code.visualstudio.com)
- [Java v. 17](https://www.oracle.com/pe/java/technologies/downloads/)
- [Spring Boot](https://spring.io/projects/spring-boot)
- [Maven](https://maven.apache.org)
- [PostgreSQL](https://www.postgresql.org)
- [ElephantSQL](https://www.elephantsql.com)
- [Postman](https://www.postman.com)
- Most important dependencies or libraries:
    - [MapStruct](https://mapstruct.org)
    - [Swagger](https://swagger.io)
    - [Lombok](https://projectlombok.org)
    - [JUnit 5](https://junit.org/junit5/)

## Project Structure

The project was divided into six packages: controller, dto, entity, mapper, repository, service; each of these had its corresponding entities. Additionally, outside of these packages, there is the main class (ProjectLoanApplication) and the SwaggerConfig entity. Finally, there is a test folder where unit tests were performed for some cases.

## Project Configuration

As a starting point, the [Spring Initializr](https://plugins.jetbrains.com/plugin/20212-spring-initializr) plugin for IntelliJ IDEA was used, version 3.2.0 of [Spring Boot](https://spring.io/projects/spring-boot), and Maven for Jar dependencies as Packaging.
Used dependencies:
- Spring Boot DevTools
- Lombok
- Spring Web
- Rest Repositories
- JDBC API
- Spring Data JPA
- H2 Database
- PostgreSQL Driver
- Validation
- etc.
  For more information, please review the pom.xml file.

## Project Execution

To execute the project, consider the following:
- An ElephantSQL-supplied database is being used.
- Database connection configuration is in the application.properties file.
- The default port is 8080.
- Postman is used for GET, POST, DELETE, etc. requests.
- For the Swagger interface after project execution, follow this [link](localhost:8080/swagger-ui/index.html#/).

## Usage

The usage of this project will allow, through three blocks (customers, requests, and loans), adding to the database the loans that have successfully met the business rules established within the methods of the **service** package.

## Considerations

As indicated at the beginning of this file, this is a simple project to demonstrate the utility of the technologies used within this project, which means not all technologies have been deployed with in-depth exploration.