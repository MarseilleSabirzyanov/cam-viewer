package ru.kmpo.camviewer.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Size;

@MappedSuperclass
@Data
public class AuthUser extends BaseEntity {
    @Size(min = 3, message = "Имя пользователя должно состоять минимум из 3 символов")
    private String username;

    @Size(min = 8, message = "Пароль не может быть короче 8 символов")
    private String password;

    @Transient
    private String confirmPassword;
}
