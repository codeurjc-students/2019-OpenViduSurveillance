package com.example.repository;

import com.example.entity.Camera;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.CrossOrigin;

import java.util.List;

@CrossOrigin
@Repository
public interface CameraRepository extends CrudRepository<Camera,Integer> {
    Camera getCameraBySessionAndData(String session, String data);
    List<Camera> getCamerasByData(String data);
    Boolean existsCameraBySessionAndRtspUri(String session, String rtspUri);
    List<Camera> getCamerasBySession(String session);
    Boolean existsCameraBySession(String session);
}
