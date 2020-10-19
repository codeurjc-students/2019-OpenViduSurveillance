package urjc.ovback.openViduBackend;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import urjc.ovback.controller.SessionController;
import urjc.ovback.entity.Camera;
import urjc.ovback.error.CameraAlreadyInSessionException;
import urjc.ovback.repository.CameraRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import urjc.ovback.repository.UserRepository;
import urjc.ovback.security.Encoder;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTest {
    @Autowired
    CameraRepository cameraRepository;
    @Autowired
    Encoder encoder;
    @Autowired
    UserRepository userRepository;
    @Autowired
    SessionController sessionController = new SessionController(cameraRepository, encoder, userRepository);

    @Test
    public void newCameraAndDatabaseConnection() {
        String rtspUri = "rtsp://test.v1";
        String cameraName = "cameraNameTest0";
        String sessionName = "sessionNameTest0";
        Camera camera = new Camera(rtspUri, cameraName, sessionName);
        cameraRepository.save(camera);
        assert (cameraRepository.getCameraBySessionAndData(sessionName, cameraName)
                .getRtspUri().equals(camera.getRtspUri()));
        cameraRepository.delete(camera);
        assert (cameraRepository.getCameraBySessionAndData(sessionName,cameraName) == null);
    }

    @Test
    public void newSessionIncorrect() {
        try {
            sessionController.start("New Session");
        }
        catch (Exception e){
            assert e.getMessage() == "Session name can only be alphanumeric without spaces\n";
        }
    }
    @Test
    public void newSessionCorrect() {
        try {
            sessionController.start("NewSession");
        } catch (Exception e){
            assert e == null;
        }
    }
}
