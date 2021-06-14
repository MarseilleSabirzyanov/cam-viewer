package ru.kmpo.camviewer.service;

import ru.kmpo.camviewer.domain.Camera;
import ru.kmpo.camviewer.domain.User;

import java.util.List;

public interface CameraService {

    Camera register(User cameraUser);

    List<Camera> getAll();

    Camera findByUsername(String username);

    Camera findById(Long id);

    void delete(Long id);

    Camera findByUserId(Long id);
}
