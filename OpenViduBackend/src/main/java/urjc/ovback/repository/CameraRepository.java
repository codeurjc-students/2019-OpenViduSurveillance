package urjc.ovback.repository;

import urjc.ovback.entity.Camera;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Repository
public interface CameraRepository extends CrudRepository<Camera,Integer> {
    Camera getCameraBySessionAndData(String session, String data);
    Boolean existsCameraBySessionAndRtspUri(String session, String rtspUri);
    List<Camera> getCamerasBySession(String session);
    List<Camera> getCamerasBySessionAndData(String sesssion, String data);
    Boolean existsCameraBySession(String session);
    Boolean existsCameraBySessionAndData(String session, String data);

}
