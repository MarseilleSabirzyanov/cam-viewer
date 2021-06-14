package ru.kmpo.camviewer.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.kmpo.camviewer.domain.User;

public interface UserService {

    User register(User user);

    Page<User> getAll(Pageable pageable);

    User findByUsername(String username);

    User findById(Long id);

    void delete(Long id);

    User createUser(User user, String role);

    User addCameraToUser(Long userId, Long cameraId);

    User removeCameraFromUser(Long userId, Long cameraId);

    User updateUser(User user);

    Page<User> getAll(Pageable pageable, String searchText);

    User addRole(Long userId, String name);

    User removeRole(Long userId, String roleName);
}
