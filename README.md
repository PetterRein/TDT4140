# How Are You Feeling

Vi skal lage et program for helsepersonell som har mulighet til å samle data om hvordan en pasient føler seg, og som analyserer og presenterer dette for relevant helsepersonell. Dette kan brukes i mange ulike deler av helsesektoren hvor det er behov for å spore en pasients behandlingsprosess. 
Pasienten velger selv hvor mye data som skal samles og hva som skal presenteres for helsepersonellet. 


## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.

### Prerequisites

What things you need to install the software

```
Install Tomcat 8 or 9.
```

### Installing

A step by step guide to run the server and UI
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

## Built With

* [Tomcat](http://tomcat.apache.org/) - Web Deployment Service
* [Maven](https://maven.apache.org/) - Dependency Management

