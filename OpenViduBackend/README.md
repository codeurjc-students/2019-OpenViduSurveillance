# OpenViduSurveillance Backend

## What does this application do?
This is the backend for OpenVidu Surveillance. This will manage all the communications between the user and OpenVidu. To see a tutorial on how to use this app check the [general readme](https://github.com/codeurjc-students/2019-OpenViduSurveillance/blob/master/README.md#how-do-i-use-it-)

## How do i use it ?
This backend works as an API REST, so you just make HTTP petitions from the frontend and this will answer you. You can ask to put cameras on your app, delete them, start a new session, discover local cameras and move them if you have direct access. 

## Configuration

I use this with a MySQL database, so you will need to configue MySQL in case you want to use this backend like me.

Remember to add this to you application.properties>
```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DB_NAME?createDatabaseIfNotExist=true
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

## Entities
### Camera
This entity is used mostly to store cameras and to manage them inside the backend.

### User
This entity is used to store an user in the database. You'll be able to login in the app using an user and password that exists in the database

## Controllers

### SessionController
This controller takes petitions to manage sessions and cameras.

#### @GetMapping("/session/{sessionId}")
    public String sessionInfo(@PathVariable String sessionId)
This method will get us information from the session named as "sessionId".

#### @GetMapping("/newSession/{sessionId}")
    public String start(@PathVariable String sessionId)
With this method we will send a request to OpenVidu-Server to initialize a new session names as "sessionId".

#### @PostMapping("/session/{sessionId}/addIpCamera")
    public String newCamera(@PathVariable String sessionId, @RequestBody IpCamera ipCamera)
With this method we will send a request to OpenVidu-Server to add an IP camera to our session. This method takes a body JSON with the same parameters as described here : https://docs.openvidu.io/en/2.12.0/reference-docs/REST-API/#post-apisessionsltsession_idgtconnection 
    
### CamerasPTZController
This controller takes care of cameras that you have total access to. 

#### @PostMapping("/add")
    public String addNewCamera(@RequestBody String url)
Adds a new camera using the requested url.
#### @PostMapping("/addDemoCameras")
    public void addDemoCameras(@RequestBody String sessionName)
Adds to the session some demo cameras just to try the app and the connection.
####  @PostMapping("/discover")
    public String  discoverCameras(@RequestBody String sessionName) 
Discover cameras in you local network.
####  @GetMapping("/localCameras")
    public List<Camera> localCameras()
Returns a list of discovered local cameras.
#### @GetMapping("/discover/cam")
    public void discoverCam()
#### @PostMapping("/ptz/{hostIp}")
    public boolean ptz(@RequestParam String user, @RequestParam String password, @PathVariable String hostIp)
#### @PostMapping("/ptz/{hostIp}/{direction}")
    public void ptzMovement(@RequestParam String user, @RequestParam String password, @PathVariable String hostIp, @PathVariable String direction)   
