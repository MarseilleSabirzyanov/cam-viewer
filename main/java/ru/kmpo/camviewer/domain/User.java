package ru.kmpo.camviewer.domain;

import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@Entity
@Table(name = "usr")
@Data
public class User extends AuthUser {
    @Email
    @Column(name = "email")
    private String email;

    @Column(name = "first_name")
    private String firstName;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name = "usr_role",
            joinColumns = {@JoinColumn(name = "user_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")})
    private List<Role> roles;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL,
                fetch = FetchType.LAZY, optional = false)
    private Camera camera;

    @ManyToMany(mappedBy = "usersCamera")
    private List<Camera> userCameras;

    @Override
    public String toString() {
        return getUsername();
    }
}
