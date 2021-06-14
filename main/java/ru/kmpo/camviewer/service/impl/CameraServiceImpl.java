package ru.kmpo.camviewer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.kmpo.camviewer.domain.CamStatus;
import ru.kmpo.camviewer.domain.Camera;
import ru.kmpo.camviewer.domain.Status;
import ru.kmpo.camviewer.domain.User;
import ru.kmpo.camviewer.exception.NotFoundException;
import ru.kmpo.camviewer.repos.CameraRepo;
import ru.kmpo.camviewer.repos.RoleRepo;
import ru.kmpo.camviewer.repos.UserRepo;
import ru.kmpo.camviewer.service.CameraService;
import ru.kmpo.camviewer.service.UserService;

import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class CameraServiceImpl implements CameraService {
    private final CameraRepo cameraRepo;
    private final RoleRepo roleRepo;
    private final UserRepo userRepo;
    private final UserService userService;
    private BCryptPasswordEncoder passwordEncoder;

    public CameraServiceImpl(CameraRepo cameraRepo, RoleRepo roleRepo, UserRepo userRepo, UserService userService) {
        this.cameraRepo = cameraRepo;
        this.roleRepo = roleRepo;
        this.userRepo = userRepo;
        this.userService = userService;
    }

    public void setPasswordEncoder(BCryptPasswordEncoder passwordEncoder) {
        this.passwordEncoder = passwordEncoder;
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }

    @Override
    @Transactional
    public Camera register(User cameraUser) {
        User registeredUser = userService.createUser(cameraUser, "ROLE_CAMERA");

        Camera registeredCamera = new Camera();

        registeredCamera.setUser(registeredUser);
        registeredCamera.setCreated(new Date());
        registeredCamera.setUpdated(new Date());
        registeredCamera.setStatus(Status.ACTIVE);
        registeredCamera.setIsActivated(false);
        registeredCamera.setShootingDelayInMilliseconds(300000L);
        registeredCamera.setIsRadiatorActive(false);
        registeredCamera.setCamStatus(CamStatus.OFFLINE);
        Camera result = cameraRepo.save(registeredCamera);

        log.info("IN register - camera: {} successfully registered", result);

        return result;
    }

    @Override
    public List<Camera> getAll() {
        List<Camera> result = cameraRepo.findAll();

        log.info("IN getAll - {} cameras found", result.size());

        return result;
    }

    @Override
    public Camera findByUsername(String username) {
        User user = userRepo.findByUsernameIgnoreCase(username).orElseThrow(() -> new NotFoundException("Пользователь не найден"));
        Camera camera = cameraRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Камера не найдена"));

        log.info("IN findByUsername - camera: {} - found", camera);

        return camera;
    }

    @Override
    public Camera findById(Long id) {
        Camera camera = cameraRepo.findById(id).orElseThrow(() -> new NotFoundException("Камера не найдена"));

        log.info("IN findById - camera: {} - found", camera);

        return camera;
    }

    @Override
    public void delete(Long id) {
        Camera camera = cameraRepo.findById(id).orElseThrow(() -> new NotFoundException("Камера не найдена"));

        camera.setStatus(Status.DELETED);

        log.info("IN delete - camera with id: {} was deleted", id);
    }

    @Override
    public Camera findByUserId(Long id) {
        return cameraRepo.findByUserId(id).orElseThrow(() -> new NotFoundException("Камера не найдена"));
    }
}
