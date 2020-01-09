# Openvidu Surveillance
### To start this app you will need:
1) To start both Kurento Media Server and Openvidu Server.
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
2) Clone, install and run the app
- Then clone this repo
```
git clone https://github.com/OpenVidu/openvidu-tutorials.git
```
- You will need angular-cli
```
npm install -g @angular/cli@6.2.4

```
- And finally run the tutorial
```
cd 2019-OpenViduSurveillance/openvidu-surveillance-angular
npm install
ng serve
```
