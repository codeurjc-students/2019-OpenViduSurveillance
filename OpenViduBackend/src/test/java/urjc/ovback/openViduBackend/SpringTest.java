package urjc.ovback.openViduBackend;

import urjc.ovback.entity.Camera;
import urjc.ovback.repository.CameraRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class SpringTest {

    @Autowired
    CameraRepository cameraRepository;

    @Test
    public void databaseConnection() {
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
}
