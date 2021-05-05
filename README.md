# Link Social Media

## Project Description
Link is the second iteration of a social media application created by Team Avatar as part of the Revature Full Stack Angular training program in April 2021. Developed with a microservice architecture, this web application allows every user to follow each other and view each other's posts. Each user has their own account that they can customize with their own information. Within this network, users can interact with each other through comments, likes, and a global chatroom. This social media application is meant to ease the transition of becoming a Revature employee.

ChatService allows users to access a live global chatroom and message other users. Users can see who is online and can send and recieve messages live.

## Technologies Used

- Spring Boot
	- Euereka Discovery Client
	- Spring Web MVC
	- Spring Websocket
	- Lombok
- Java - version 1.8
- JUnit
- Log4J
- H2 Database Testing

## Features

- View a list of online users.
- View a global chat feed that is updated live with websockets.
- Send text messages to the live chat feed.

## Getting Started
   
> Clone this repository
```
git clone https://github.com/LinkSocialNetwork/ChatService.git
```

> Clone Eureka, Gateway, and UserService services
```
git clone https://github.com/LinkSocialNetwork/Eureka.git
git clone https://github.com/LinkSocialNetwork/Gateway.git
git clone https://github.com/LinkSocialNetwork/UserService.git
```

> Clone FrontendClient
```
git clone https://github.com/LinkSocialNetwork/FrontendClient.git
```

> npm install in angular project folder
```
npm i FrontendClient/Angular
```

## **Usage**

> Run the services together, sequentially in an IDE
```
Eureka > Gateway > UserService > ChatService
```

> Visit the Eureka url and confirm Eureka can see the Gateway, UserService, and ChatService
```
http://localhost:9999/Eureka
```

> Run angular project
```
cd FrontendClient/Angular
npm run start
```

> Visit the frontend url
```
http://localhost:4200
```

## **License**

This project uses the following license: [<The MIT License>](https://www.mit.edu/~amini/LICENSE.md).
