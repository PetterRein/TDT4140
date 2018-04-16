## Melding til studass

For å se prosjektet som det var ved innlevering sprint 3 se tag V.3

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
Download release and extract, see releasev3.0gr44.zip in root of master.
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
The need to run your own tomcat is no more because a running server is up at api.moholt.me but the instructions remain if you want to, but then you have to change the url in the source code
```
```
Run 44.jar
```
You should now have server running and see the login page.
Available users in sample.db is:
petter@email.com with password password,
admin@email.com with password password,
haavard@mail.com with password password, remember that the patient ui is not a part of the project but is here if you want to check a patients data. The api supports every function a patient needs so any application that implement the api can be used by a patient for rapporting.

## Running the tests
To test all of the sub-modules in our project you can simply run _mvn test_ or _mvn install_

## Built With

* [JSON In Java](https://mvnrepository.com/artifact/org.json/json) - Data interchange format
* [JavaFX](www.ntnu.no/wiki/display/tdt4100/JavaFX) - GUI Framework for building GUIs
* [SQLite](https://www.sqlite.org/index.html) - Embedded SQL database
* [Tomcat](http://tomcat.apache.org/) - Web Deployment Service
* [Maven](https://maven.apache.org/) - Dependency Management
* [Apache HttpComponents] (hc.apache.org) - The Apache HttpComponents™ project is responsible for creating and maintaining a toolset of low level Java components focused on HTTP and associated protocols.
* [jBCrypt] (https://github.com/jeremyh/jBCrypt) - jBCrypt is an implementation the OpenBSD Blowfish password hashing algorithm, as described in "A Future-Adaptable Password Scheme" by Niels Provos and David Mazieres.
* [JavaServlet] (https://mvnrepository.com/artifact/javax.servlet/servlet-api) - 

