package ru.kmpo.camviewer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import ru.kmpo.camviewer.domain.Camera;
import ru.kmpo.camviewer.domain.User;
import ru.kmpo.camviewer.dto.CameraDto;
import ru.kmpo.camviewer.dto.Views;
import ru.kmpo.camviewer.security.Jwt.JwtUser;
import ru.kmpo.camviewer.service.CameraService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping(value = "/api/v1/camera")
public class CameraController {
    private CameraService cameraService;

    public CameraController(CameraService cameraService) {
        this.cameraService = cameraService;
    }

    @JsonView(Views.camera.class)
    @GetMapping(value = "/all")
    public ResponseEntity<List<CameraDto>> getAll() {
        List<CameraDto> cameraDtos = cameraService.getAll().stream()
                .map(CameraDto::fromCamera)
                .collect(Collectors.toList());

        return new ResponseEntity<>(cameraDtos, HttpStatus.OK);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<CameraDto> getCameraById(@PathVariable(name = "id") Long id) {
        Camera camera = cameraService.findById(id);

        return new ResponseEntity<>(CameraDto.fromCamera(camera), HttpStatus.OK);
    }

    @PreAuthorize("hasAuthority('CAMERA')")
    @GetMapping(value = "/profile")
    public ResponseEntity<CameraDto> getProfile(@AuthenticationPrincipal JwtUser user) {
        Camera camera = cameraService.findByUserId(user.getId());

        return new ResponseEntity<>(CameraDto.fromCamera(camera), HttpStatus.OK);
    }

    @PostMapping(value = "/register")
    public ResponseEntity<CameraDto> addCamera(@RequestBody User user) {
        Camera camera = cameraService.register(user);
        return new ResponseEntity<>(CameraDto.fromCamera(camera), HttpStatus.OK);
    }
}
