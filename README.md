# 2019-OpenViduSurveillance
## Medium
To see how i worked in this app step by step, check my [Medium page ](https://medium.com/@igort142)
## Phase 1 - Experimental / exploratory phase

In this project we are going to develop a web app in order to manage and connect IP cameras using the RTSP and ONVIF protocols.
In this first stage, we will focus on experimenting with a camera, try to connect it using some simple web app and make use of the ONVIF protocol to move the camera.

## Phase 2 - Divide in Frontend and Backend

Once the app is functional, its time to implement correctly a frontend and a backend. For the frontend we'll use Angular and for the backend we'll use Spring Boot. Additionally to this, the backend will have a MySQL database to store data for the different sessions and cameras.
Both of them will have a separate README inside this project.


## Running the app
Follow these steps before you run the backend and frontend: 
1) Start both Kurento Media Server and Openvidu Server.
- For the Kurento Media Server you can use this docker image 
```
docker run --rm -p 8888:8888 kurento/kurento-media-server:6.12)
```
- Then Openvidu Server, which you can get from [OpenVidu Server](https://github.com/OpenVidu/openvidu/tree/master/openvidu-server)
. The easiest way is to clone, compile and run it using these commands:
```
git clone https://github.com/OpenVidu/openvidu.git
cd openvidu
mvn -DskipTests=true clean install # Install global dependencies
cd openvidu-server
mvn exec:java
```
2) Start the [Frontend](https://github.com/codeurjc-students/2019-OpenViduSurveillance/tree/master/openvidu-surveillance-angular) and [Backend](https://github.com/codeurjc-students/2019-OpenViduSurveillance/tree/master/OpenViduBackend)
- If you have problems starting these, check their Readme.
