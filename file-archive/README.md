### Installation

Install commons and client libs to your maven path

```
cd file-archive-commons
mvn install

cd file-archive-jaxrs
mvn install
```

Install and run Glassfish 4 locally

Install Archive service

```
cd file-archive-service
mvn clean package cargo:deploy
```

Install WebApp

```
cd file-archive-webapp
mvn clean package cargo:deploy
```
