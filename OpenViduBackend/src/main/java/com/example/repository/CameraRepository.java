package com.example.repository;

import com.example.entity.Camera;
import org.springframework.data.repository.CrudRepository;


public interface CameraRepository extends CrudRepository<Camera,Integer> {
}
