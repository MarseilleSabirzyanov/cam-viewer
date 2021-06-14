package ru.kmpo.camviewer.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import lombok.Data;
import ru.kmpo.camviewer.dto.Views;

@JsonIgnoreProperties
@Data
public class RoleDto {
    @JsonView(Views.withCamera.class)
    private Long id;
    @JsonView(Views.withCamera.class)
    private String name;

    public Role toRole() {
        Role role = new Role();
        role.setId(id);
        role.setName(name);

        return role;
    }

    public static RoleDto fromRole(Role role) {
        RoleDto roleDto = new RoleDto();
        roleDto.setId(role.getId());
        roleDto.setName(role.getName());

        return roleDto;
    }
}
