package ru.kmpo.camviewer.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ru.kmpo.camviewer.domain.Camera;
import ru.kmpo.camviewer.domain.Role;
import ru.kmpo.camviewer.domain.RoleDto;
import ru.kmpo.camviewer.domain.User;

import java.util.List;
import java.util.stream.Collectors;

@JsonIgnoreProperties
@Data
public class UserDto {
    @JsonView(Views.withoutCamera.class)
    private Long id;
    @JsonView(Views.withoutCamera.class)
    private String username;
    @JsonView(Views.withoutCamera.class)
    private String firstName;
    @JsonView(Views.withoutCamera.class)
    private String email;
    @JsonView(Views.withCamera.class)
    private List<CameraDto> userCameras;
    @JsonView(Views.withCamera.class)
    private List<RoleDto> userRoles;

    public User toUser() {
        User user = new User();
        user.setId(id);
        user.setUsername(username);
        user.setFirstName(firstName);
        user.setEmail(email);

        if (userCameras != null) {
            List<Camera> cameras = userCameras.stream()
                    .map(CameraDto::toCamera)
                    .collect(Collectors.toList());
            user.setUserCameras(cameras);
        }

        if (userRoles != null) {
            List<Role> roles = userRoles.stream()
                    .map(RoleDto::toRole)
                    .collect(Collectors.toList());
            user.setRoles(roles);
        }

        return user;
    }

    public static UserDto fromUser(User user) {
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setUsername(user.getUsername());
        userDto.setFirstName(user.getFirstName());
        userDto.setEmail(user.getEmail());

        if (user.getUserCameras() != null) {
            List<CameraDto> cameras = user.getUserCameras().stream()
                    .map(CameraDto::fromCamera)
                    .collect(Collectors.toList());
            userDto.setUserCameras(cameras);
        }

        if (user.getRoles() != null) {
            List<RoleDto> roles = user.getRoles().stream()
                    .map(RoleDto::fromRole)
                    .collect(Collectors.toList());
            userDto.setUserRoles(roles);
        }

        return userDto;
    }
}
