package urjc.ovback.error;

public class CameraAlreadyInSessionException extends Exception {
    public CameraAlreadyInSessionException(String message) {
        super(message);
    }
}
