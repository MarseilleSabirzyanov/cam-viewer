package ru.kmpo.camviewer.domain;

import lombok.Data;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "camera")
@Data
public class Camera extends BaseEntity{

    @Column(name = "description")
    private String description;

    @Column(name = "is_activated")
    private Boolean isActivated;

    @Column(name = "shooting_delay")
    private Long shootingDelayInMilliseconds;

    @Column(name = "is_radiator_active")
    private Boolean isRadiatorActive;

    @Column(name = "cam_status")
    @Enumerated(value = EnumType.STRING)
    private CamStatus camStatus;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @OneToMany(mappedBy = "camera", cascade = CascadeType.ALL,
            fetch = FetchType.LAZY)
    private List<FileStorage> files;

    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(
            name = "usr_camera",
            joinColumns = @JoinColumn(name = "camera_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id"))
    private List<User> usersCamera;
}
