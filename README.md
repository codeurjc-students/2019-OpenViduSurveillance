# 2019-OpenViduSurveillance

## What does this application do?
With this web application you can create sessions where you can stream video from IP cameras in real time through RTSP ( Real time streaming protocol ).

## How do i use it ?
To access the visual interface, just go from your browser to where your OpenVidu Surveillance application is deployed. If you're developing the app, by default you can access the frontend at "http://localhost:4200"
Then you will see this screen 

![OVS Login](https://github.com/codeurjc-students/2019-OpenViduSurveillance/blob/master/Documentation/OpenViduSurveillanceLogin.png)

From here you can log in to the application, by default you'll have two different users, user with the password user and admin with the password admin. **Change this in the backend**.

Then connect to a session, no matter the name, just keep in mind, only alphanumeric without spaces are allowed.

![OVS Join session](https://github.com/codeurjc-students/2019-OpenViduSurveillance/blob/master/Documentation/OpenViduSurveillanceSession.png)

Here you will see our main screen, if it's the first time you enter this session ( the session is empty as it has no cameras ) you'll see this message, just to help you know what to do next.

![OVS Main screen](https://github.com/codeurjc-students/2019-OpenViduSurveillance/blob/master/Documentation/OpenViduSurveillanceMainScreenFirstTime.png)

Then, from the administration panel ( in the cog icon ) you can **add**, **delete** or **discover** new cameras.

![OVS Administration panel](https://github.com/codeurjc-students/2019-OpenViduSurveillance/blob/master/Documentation/OpenViduSurveillanceAdministrationPanel.png)

If you slide the "Add camera with PTZ function" you'll have to enter some more data, as the PTZ functions require to authenticate in to the camera. Also, you'll only be able to delete cameras if you already have some!

![OVS Administration panel extra functions](https://github.com/codeurjc-students/2019-OpenViduSurveillance/blob/master/Documentation/OpenViduSurveillanceAdministrationPanelAlt.png)

Then you can go back to the main screen and you'll see the cameras you added, if you have total access to this cameras and its a camera compatible with PTZ functions you'll also see the direction controls.

![OVS Administration panel extra functions](https://github.com/codeurjc-students/2019-OpenViduSurveillance/blob/master/Documentation/OpenViduSurveillanceMainScreenPTZ.png)


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

You'll also need [**this external library**](https://github.com/milg0/onvif-java-lib/tree/master/release) . This library manages the PTZ operations with your local cameras, this library has to be added as an external library. You can find more about it [here](http://www.milgo.de/onvif/java-library/create-connection)

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
You can deploy OpenVidu Surveillance using docker compose. You can check the docker-compose.yml and you should use your own .env file.
From inside the directory run 
```
 docker-compose up --build
```

This will build and deploy the app.You can check [how to set up OpenVidu configuration](https://docs.openvidu.io/en/2.15.0/reference-docs/openvidu-config/) using an .env file.
You should check these variables when setting up OpenVidu Surveillance:

### OpenVidu
- **DOMAIN_OR_PUBLIC_IP:** Domain name from where OpenVidu will be available, or the IP of the machine, if you don't have one.
- **OPENVIDU_SECRET:** Secret used to connect to OpenVidu Server.

### MySQL 
- **MYSQL_DATABASE:** Name of your database.
- **MYSQL_USER:** Name of the database user.
- **MYSQL_PASSWORD:** Password of your user to access.

### Spring Boot
- **OPENVIDU_URL:** Where your OpenVidu is deployed. You can set it up like "https://openvidu-server-kms:PORT/" if you're deploying everything in the same machine.

The rest of the Spring Boot variables are the same that you use in the application.properties of the backend ( URL, USERNAME, PASSWORD ).

### Angular 
- **OPENVIDUSURVEILLANCE_BACKEND_URL:** Where your backend is deployed. Same as before, if you're deploying everything in the same machine you can set it up like this "https://spring-service:PORT/

