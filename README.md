## Description

This is an example of using the PATCH http verb within a Jersey/Spring stack

* Java 8
* Spring Boot
* Jersey
* Hibernate
* Jackson
* Spring DI
* Postgresql

## Install Postgresql
The example is using PostgreSQL 9.6+ and can be installed quite easily on mac, linux or windows following a [guide](https://www.codefellows.org/blog/three-battle-tested-ways-to-install-postgresql)

## Create the database user

```
CREATE ROLE "SpringBootUser" LOGIN
  ENCRYPTED PASSWORD 'md513445691374efba1aaee7b0912e63af3'
  SUPERUSER INHERIT CREATEDB NOCREATEROLE NOREPLICATION;
```

## Create the database

```
CREATE DATABASE "SpringJerseyPatchExample"
  WITH ENCODING='UTF8'
       OWNER="SpringBootUser"
       CONNECTION LIMIT=-1;
```

## Build the project

```
mvn clean install
```

## Run the migrations

```
mvn liquibase:update -P local
```

## Running the API

Start the service by running the following command:

```
java -jar target/spring-jersey-patch-example-1.0-SNAPSHOT.jar
```

You can now test the service by consuming the api on port 8080. Some routes you can try in your browser (GET requests):


* http://127.0.0.1:8080/members

* http://127.0.0.1:8080/members/2


For the PATCH request, here is a sample:

```
POST 127.0.0.1:8080/members/2
Content-Type: application/json-patch+json
[{
  "op": "replace",
  "path": "/lastName",
  "value": "NewLastName"
}]
```
## License

Copyright © 2016 Tyler Hoersch

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
