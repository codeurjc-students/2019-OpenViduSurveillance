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
    public ResponseEntity start(@PathVariable String sessionId)
With this method we will send a request to OpenVidu-Server to initialize a new session named as "sessionId". It will return us the status of the petition.

#### @PostMapping("/session/{sessionId}/camera")
    public JsonNode addCamera(@RequestBody Camera camera, @PathVariable String sessionId)
Adds a new camera using the requested url, after checking the url and the camera name. The url has to start with rtsp:// and the camera name can only be alphanumeric, without spaces and unique.

#### @DeleteMapping("/session/{sessionId}/cameras/{cameraName}")
    public void deleteCamera(@PathVariable String cameraName, @PathVariable String sessionId)
This deletes a camera from the database and your session.

#### @GetMapping("/session/{sessionId}/cameras")
    public List<Camera> getCameras(@PathVariable String sessionId)
This gives you a list of all the cameras attached to "sessionId".
    
### CamerasPTZController
This controller takes care of cameras that you have total access to. 

#### @GetMapping("/{sessionName}/localCameras")
    public List<Camera> discoverCameras(@PathVariable String sessionName) 
Gets you a list of all the cameras available in your network.
    
#### @GetMapping("{sessionName}/cameras/{cameraName}/ptz")
    public boolean ptzAvailable(@PathVariable String sessionName, @PathVariable String cameraName)
This method returns true if PTZ functions are available in the camera that goes by the name in the url.
    
#### @GetMapping("{sessionName}/cameras/{cameraName}/{direction}")
    public void ptzMovement(@PathVariable String sessionName, @PathVariable String cameraName, @PathVariable String direction)
This method moves the camera named as the variable "cameraName". Direction can be "up", "down", "left" or "right".

### Repository
In this package we can find CameraRepository and UserRepository. This repositories enable us to work with the database from the backend. 

### Security
Here we can find our security configuration. There are some user established as default, so when you use this app for your need **delete them and use your own**, as this information is public. 
