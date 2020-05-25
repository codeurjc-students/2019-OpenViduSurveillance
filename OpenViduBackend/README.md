# OpenViduSurveillance Backend

This is the Spring boot backend for the frontend Angular Openvidu App.
To run this just open the project as a normal Spring app.

## Configuration

I use this with a MySQL database, so you will need to configue MySQL in case you want to use this backend like me.

Remember to add this to you application.properties>
```
spring.jpa.hibernate.ddl-auto=update
spring.datasource.url=jdbc:mysql://localhost:3306/YOUR_DB_NAME?createDatabaseIfNotExist=true
spring.datasource.username=YOUR_DB_USERNAME
spring.datasource.password=YOUR_DB_PASSWORD
```

## Starting the backend

Before you start the app make sure you configured it correctly and you have an active [MySQL](https://dev.mysql.com/downloads/) database running to use with this application. If you have trouble setting up MySQL you can check the docs [here](https://dev.mysql.com/doc/).
Then you just have to run the app.

## Entities
### Camera
This entity is used mostly to store cameras and to manage them inside some functions.
- session ( String ) : session name.
- url ( String ) : url used to access the camera.
- camera ( String ) : name for the camera.

### IpCamera
This entity is used to subscribe a camera to a session using Openvidu Server.
- rtspUri ( String ) : url of the cameras.
- data ( String ) : additional information attached to the camera, I use it mostly for the camera name.
- adaptativeBitrate ( Boolean ).
- onlyPlayWhenSubscribers ( Boolean ).

## Controllers

### SessionController
This controller takes petitions to manage sessions and cameras.
#### @GetMapping("/saludo")
    public String index()
This method is just to check if the backend is running properly and has a connection with the frontend, will be removed in the future.

#### @GetMapping("/session/{sessionId}")
    public String sessionInfo(@PathVariable String sessionId)
This method will get us information from the session named as "sessionId".

#### @GetMapping("/newSession/{sessionId}")
    public String start(@PathVariable String sessionId)
With this method we will send a request to OpenVidu-Server to initialize a new session names as "sessionId".

#### @PostMapping("/session/{sessionId}/addIpCamera")
    public String newCamera(@PathVariable String sessionId, @RequestBody IpCamera ipCamera)
With this method we will send a request to OpenVidu-Server to add an IP camera to our session. This method takes a body JSON with the same parameters as described here : https://docs.openvidu.io/en/2.12.0/reference-docs/REST-API/#post-apisessionsltsession_idgtconnection

#### @GetMapping("/discover/cam")
    public void discoverCam()
#### @PostMapping("/ptz/{hostIp}")
    public boolean ptz(@RequestParam String user, @RequestParam String password, @PathVariable String hostIp)
####  @PostMapping("/ptz/{hostIp}/{direction}")
    public void ptzMovement(@RequestParam String user, @RequestParam String password, @PathVariable String hostIp, @PathVariable String direction)    
    
    
### DatabaseController
This controller takes care of operations with the database.

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
