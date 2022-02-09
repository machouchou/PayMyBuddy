# Pay My Buddy
Openclassrooms project number 6

<!-- ABOUT THE PROJECT -->
## About The Project

Paiement application Pay My Buddy. Th

Project goals:
* Create database tables.
* UML class diagram 
* SQL class diagram
* SQL scripts to create tables 

![UML-class-diagram](/images/DiagSqlPayMyBuddy.png)

![database](/images/DiagUmlPayMyBuddy.png)


### Built With

* Framework: Spring Data JPA
* Java 08
* Bootstrap
* Angular CLI

<!-- GETTING STARTED -->
## Getting Started

This is how to set up the project locally.
To get a local copy up and running follow these simple example steps:

### Prerequisites


4. Package the application (fat jar file) using [maven wrapper](https://github.com/takari/maven-wrapper) provided in the folder, it downloads automatically the correct Maven version if it's not found.
   ```sh
   mvnw package
   ```
5. Execute the jar file
   ```JS
   java -jar ./target/paymybuddy-0.0.1-SNAPSHOT.jar
   ```
6. To access the application, open your browser, go to [http://localhost:8080](http://localhost:8080)

7. Note that the first time, since you start with an empty database, you need to [register](http://localhost:8080/registration) some users to be able to do some operations.