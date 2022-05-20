# Superhero Sighting 

Web application created following MVC design pattern to devlope database and data layer for superhero sighting project
* JdbcTemplate
* Thymeleaf
* Bootstrap


## Entity Relationship Diagram
![a;t-text](https://github.com/lindaerin/supersighting-spring-boot/blob/main/Diagram/ERD.png)


## Project Structure
```
supersighting
 ┣ controller
 ┃ ┣ HeroController.java
 ┃ ┣ HomeController.java
 ┃ ┣ LocationController.java
 ┃ ┣ OrganizationController.java
 ┃ ┣ SightingController.java
 ┃ ┗ SuperpowerController.java
 ┣ dao
 ┃ ┣ HeroDao.java
 ┃ ┣ HeroDaoDB.java
 ┃ ┣ LocationDao.java
 ┃ ┣ LocationDaoDB.java
 ┃ ┣ OrganizationDao.java
 ┃ ┣ OrganizationDaoDB.java
 ┃ ┣ SightingDao.java
 ┃ ┣ SightingDaoDB.java
 ┃ ┣ SuperpowerDao.java
 ┃ ┗ SuperpowerDaoDB.java
 ┣ model
 ┃ ┣ Hero.java
 ┃ ┣ Location.java
 ┃ ┣ Organization.java
 ┃ ┣ Sighting.java
 ┃ ┗ Superpower.java
 ┗ SupersightingApplication.java
 
 ```

## Instructions
* Configure applciation.properties file to match database information

```
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
spring.datasource.url=jdbc:mysql://localhost:3306/"YOUR_DB_FILE"?serverTimezone=America/Chicago&useSSL=false
spring.datasource.username="user"
spring.datasource.password="pass" 
``` 



