package ru.kmpo.camviewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ru.kmpo.camviewer.domain.CamStatus;
import ru.kmpo.camviewer.domain.Camera;

@JsonIgnoreProperties
@Data
public class CameraDto {
    @JsonView(Views.withCamera.class)
    private Long id;
    @JsonView(Views.camera.class)
    private String description;
    @JsonView(Views.camera.class)
    private Boolean isActivated;
    @JsonView(Views.camera.class)
    private Long shootingDelayInMilliseconds;
    @JsonView(Views.camera.class)
    private Boolean isRadiatorActive;
    @JsonView(Views.camera.class)
    private String camStatus;

    public Camera toCamera() {
        Camera camera = new Camera();
        camera.setId(id);
        camera.setDescription(description);
        camera.setIsActivated(isActivated);
        camera.setShootingDelayInMilliseconds(shootingDelayInMilliseconds);
        camera.setIsRadiatorActive(isRadiatorActive);
        camera.setCamStatus(CamStatus.valueOf(camStatus));

        return camera;
    }

    public static CameraDto fromCamera(Camera camera) {
        CameraDto cameraDto = new CameraDto();
        cameraDto.setId(camera.getId());
        cameraDto.setDescription(camera.getDescription());
        cameraDto.setIsActivated(camera.getIsActivated());
        cameraDto.setShootingDelayInMilliseconds(camera.getShootingDelayInMilliseconds());
        cameraDto.setIsRadiatorActive(camera.getIsRadiatorActive());
        cameraDto.setCamStatus(camera.getCamStatus().name());

        return cameraDto;
    }
}
