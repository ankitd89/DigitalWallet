DigitalWallet
=============

Developed an Rest application in scala for storing users details, bank accounts and login details in MongoDB using SpringBoot and Gradle

Run Application
Eclipse/Intellij - Import project as Gradle project and run with following command 
gradle build 
gradle run

or
gradle build
java -jar build/libs/wallet-0.1.0.jar

Once the server starts at port 8080
you can test the application using rest client such as Postman
The following are url for Rest webservices
Add user - Post - http://localhost:8080//api/v1/users
Display user - Get - http://localhost:8080//api/v1/users/{userid}
Update user - Add user - Post - http://localhost:8080//api/v1/users/{userid}
....and so on


