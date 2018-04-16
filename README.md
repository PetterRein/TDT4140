## Melding til studass

For å se prosjektet som det var ved innlevering sprint 2 se tag V.2

# How Are You Feeling

Prosjektet er opprettet som en del av faget TDT4140 våren 2018.

Vi skal lage et program for helsepersonell som har mulighet til å samle data om hvordan en pasient føler seg, og som analyserer og presenterer dette for relevant helsepersonell. Dette kan brukes i mange ulike deler av helsesektoren hvor det er behov for å spore en pasients behandlingsprosess. 
Pasienten velger selv hvor mye data som skal samles og hva som skal presenteres for helsepersonellet. 


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

We develop using IntelliJ, but other IDEs should work.
Maven is required for depency management.
We use Java 8 (JDK 9 should also work) combined with JavaFX.


### Installing

A step by step guide to run the server and UI
```
Install Tomcat 8 or 9.
```
```
Download release and extract, see releasev2.0gr44.zip in root of master.
```
```
Move sample.db to tomcat\bin
```
```
Move java.war to tomcat\webapps
```
```
Run script in tomcat\bin catalina.bat or catalina.sh with the args start or stop
```
```
Run 44.jar
```
You should now have server running and see the login page.
Available users in sample.db is:
syking@mail.co with password 123,
admin@o.com with password 33,
lege@mail.co with password 321

## Running the tests
To test all of the sub-modules in our project you can simply run _mvn test_ or _mvn install_

## Built With

* [JSON In Java](https://mvnrepository.com/artifact/org.json/json) - Data interchange format
* [TestFX](https://mvnrepository.com/artifact/org.testfx/testfx-core/4.0.1-alpha) - test framework for JavaFX apps
* [SQLite](https://www.sqlite.org/index.html) - Embedded SQL database
* [Tomcat](http://tomcat.apache.org/) - Web Deployment Service
* [Maven](https://maven.apache.org/) - Dependency Management

