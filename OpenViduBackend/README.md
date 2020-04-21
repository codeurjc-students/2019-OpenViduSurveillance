# 2019-OpenViduSurveillance Backend

This is the Spring boot backend for the frontend Angular Openvidu App.
To run this just open the project as a normal Spring app.

## Methods

### Get ("/saludo")
This method is just to check if the backend is running properly and has a connection with the frontend, will be removed in the future.

### Get ("/session/{sessionId}")
This method will get us information from the session named as "sessionId".

### Get ("/newSession/{sessionId}")
With this method we will send a request to OpenVidu-Server to initialize a new session names as "sessionId".

### Post ("/session/{sessionId}/addIpCamera")
With this method we will send a request to OpenVidu-Server to add an IP camera to our session. This method takes a body JSON with the same parameters as described here : https://docs.openvidu.io/en/2.12.0/reference-docs/REST-API/#post-apisessionsltsession_idgtconnection
