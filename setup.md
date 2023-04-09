# Setup

- Docker container for PostgreSQL
- Java 17
- VSCode and POSTMAN

## Docker Setup

1. Create Docker Image and Container for PostgreSQL Database
  - `docker container run --name stockmarketdb -e POSTGRES_PASSWORD=password -d -p 5440:5432 postgres`
  - I am using host port of 5440 in case you actually have PostgreSQL running, that will occupy your host port 5432.

2. Adding PostgreSQL script to the Docker Container
  - `docker cp stockmarket_db.sql stockmarketdb:/`
  - `docker container exec -it stockmarketdb bash`

3. Running the SQL Script in the Docker Container
  - `psql -U postgres --file stockmarket_db.sql`
  - Type `exit` to exit the docker container CLI

4. Running Docker Container on Interactive mode to access PostgreSQL (**use this for future access to database**)
  - `docker container exec -it stockmarketdb psql -U postgres`
  - In the interactive CLI docker container, run `\connect stockmarketdb`, enter `\q` to quit.

## Internal Errors

- If you ever get the above connection issue, ensure that Docker Desktop is running and the container "stockmarketdb" has started.

```
org.springframework.beans.factory.BeanCreationException: Error creating bean with name 'entityManagerFactory' defined in class path resource [org
springframework/boot/autoconfigure/orm/jpa/HibernateJpaConfiguration.class]: Invocation of init method failed; nested exception is javax.persistence
PersistenceException: [PersistenceUnit: default] Unable to build Hibernate SessionFactory; nested exception is org.hibernate.exception
JDBCConnectionException: Unable to open JDBC Connection for DDL execution
```