# 2019-OpenViduSurveillance

## What does this application do?
With this web application you can create sessions where you can stream video from IP cameras in real time through RTSP ( Real time streaming protocol ).

## Components
The application has four different parts.
### [Kurento Media Server](https://www.kurento.org/)
Kurento is a WebRTC media server and a set of client APIs making simple the development of advanced video applications for WWW and smartphone platforms. Kurento Media Server features include group communications, transcoding, recording, mixing, broadcasting and routing of audiovisual flows.

### [OpenVidu Server](https://github.com/OpenVidu/openvidu/tree/master/openvidu-server)
OpenVidu server side. It receives the remote procedure calls from openvidu-browser and manage all the media streams operations. You don't have to make direct use of it. Just to run 

### [Backend](https://github.com/codeurjc-students/2019-OpenViduSurveillance/tree/master/OpenViduBackend)
The backend is run using Spring Boot and a MySQL database. The server will act as an API REST for the frontend, communicating with OpenVidu Server and operating the database.

### [Frontend](https://github.com/codeurjc-students/2019-OpenViduSurveillance/tree/master/openvidu-surveillance-angular)
The frontend is run using Angular. From here we can log in to the application and connect to a session.

![alt text](https://github.com/codeurjc-students/2019-OpenViduSurveillance/blob/master/Documentation/OpenViduFrontBackdrawio%20(2).png)


## How do i start this application?
You have two ways to start using this application:

### As a developer ( Development )
To try this app like a developer, check the code, make some changes... you'll need to start KMS and Openvidu Server first. To do it you can use [this docker image](https://hub.docker.com/r/openvidu/openvidu-server-kms) which wraps everything necessary to work inside your network in just a single container. From there you can start the backend and the frontend. 
#### Start the Backend
Make sure you have an active [MySQL](https://dev.mysql.com/downloads/) database running to use with this application. If you have trouble setting up MySQL you can check the docs [here](https://dev.mysql.com/doc/). Then you can run the backend as any Spring Boot app.

#### Start the frontend
To start the frontend you'll have to :

- Install dependencies
```
npm install
```

- Run the app
```
ng serve
```

### As a normal user ( Production )
You can deploy [this docker image]() which contains all the components to run the application

