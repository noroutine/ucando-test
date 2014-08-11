### Build

```
(
    cd file-archive-commons
    mvn install
)

(
    cd file-archive-jaxrs
    mvn install
)

(
    cd file-archive-service
    mvn clean package
)

(
    cd file-archive-webapp
    mvn clean package
)


```

### Installation

1. Install wildfly
2. Install MySQL
3. Create database for file archives and for users
```
CREATE DATABASE userdb;
CREATE DATABASE file_archive;
CREATE USER 'userdb'@'localhost' IDENTIFIED BY 'LetMe1n!';
CREATE USER 'file-archive'@'localhost' IDENTIFIED BY 'LetMe1n!';

GRANT ALL PRIVILEGES ON userdb.* TO 'userdb'@'localhost';
GRANT ALL PRIVILEGES ON file_archive.* TO 'file-archive'@'localhost';
```

4. Run schema creation scripts from ```file-archive-service/db/schema.sql``` and ```userdb-schema.sql``` files respectively
5. In Wildfly there need to be created two datasources exposed via JNDI:
```
java:/comp/env/jdbc/FileArchiveDS,   JDBC URL: jdbc:mysql://localhost/file_archive
java:/comp/env/jdbc/UserDS           JDBC URL: jdbc:mysql://localhost:3306/userdb 
```

6. MySQL Connector shall be deployed
7. file-archive-service.war shall be deployed
8. file-archive-webapp.war shall be deployed
9. To increase file upload size configure max_allowed_packet in my.cnf and add max-post-size to wildfly http listener
### Brief Description

Project consists of four modules: two libraries and two webapps.

WebApp is communicating to File Service through Java SDK. File Service is using JPA for persistence and both are using JAX-RS for REST
File content is stored as BLOBs into the database, which doesn't work well with MySQL - expect files above 250Mb to make everything slow and failing.
Webapp and service were designed to handle files up to 1Gb but MySQL is a bad choice here. Storing content on the disk or AWS would raise the limit.

WebApp and File Service use another database for authenticating users. 

There are four users predefined with roles in braces: admin, joe, jack and jill.
Password is ```LetMe1n!```

For Service and SDK I used JAX-RS

For WebApp I used my other project Tobacco, which uses Backbone and Twitter Bootstrap for frontend, Spring, Spring Security and Tiles on backend.


