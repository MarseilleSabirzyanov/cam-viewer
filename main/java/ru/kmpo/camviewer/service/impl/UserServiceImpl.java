package ru.kmpo.camviewer.service.impl;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kmpo.camviewer.domain.Camera;
import ru.kmpo.camviewer.domain.Role;
import ru.kmpo.camviewer.domain.Status;
import ru.kmpo.camviewer.domain.User;
import ru.kmpo.camviewer.exception.AlreadyExistException;
import ru.kmpo.camviewer.exception.ForbiddenException;
import ru.kmpo.camviewer.exception.NotFoundException;
import ru.kmpo.camviewer.repos.CameraRepo;
import ru.kmpo.camviewer.repos.RoleRepo;
import ru.kmpo.camviewer.repos.UserPageAndSortRepo;
import ru.kmpo.camviewer.repos.UserRepo;
import ru.kmpo.camviewer.service.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepo userRepo;
    private final RoleRepo roleRepo;
    private final CameraRepo cameraRepo;
    private final UserPageAndSortRepo userPageAndSortRepo;

    public UserServiceImpl(UserRepo userRepo, RoleRepo roleRepo, CameraRepo cameraRepo, UserPageAndSortRepo userPageAndSortRepo) {
        this.userRepo = userRepo;
        this.roleRepo = roleRepo;
        this.cameraRepo = cameraRepo;
        this.userPageAndSortRepo = userPageAndSortRepo;
    }

    @Override
    public User register(User user) {
        return createUser(user, "ROLE_USER");
    }

    public User createUser(User user, String role) {
        if (userRepo.findByUsernameIgnoreCase(user.getUsername()).isPresent()) {
            throw new AlreadyExistException("Пользователь с таким именем уже существует");
        }

        if (user.getEmail() != null) {
            if (userRepo.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
                throw new AlreadyExistException("Пользователь с такой почтой уже существует");
            }
        }

        Role roleUser = roleRepo.findByName(role).orElseThrow(() -> new NotFoundException("Роль не найдена!"));
        List<Role> userRoles = new ArrayList<>();
        userRoles.add(roleUser);

        user.setPassword(new BCryptPasswordEncoder().encode(user.getPassword()));
        user.setRoles(userRoles);
        user.setStatus(Status.ACTIVE);
        user.setCreated(new Date());
        user.setUpdated(new Date());

        User registeredUser = userRepo.save(user);

        log.info("IN register - user: {} successfully registered", registeredUser);

        return registeredUser;
    }

    @Override
    public User addCameraToUser(Long userId, Long cameraId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        Camera camera = cameraRepo.findById(cameraId).orElseThrow(() -> new NotFoundException("Камера не найдена!"));

        if (user.getUserCameras().contains(camera)) {
            throw new AlreadyExistException("Пользователю " + user.getUsername() + " уже прикреплена камера " + camera.getUser().getUsername());
        }

        user.getUserCameras().add(camera);
        camera.getUsersCamera().add(user);

        cameraRepo.save(camera);
        userRepo.save(user);

        return user;
    }

    @Override
    public User removeCameraFromUser(Long userId, Long cameraId) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        Camera camera = cameraRepo.findById(cameraId).orElseThrow(() -> new NotFoundException("Камера не найдена!"));

        if (!user.getUserCameras().remove(camera)) {
            throw new NotFoundException("Данный пользователь не подписан на данную камеру!");
        }
        else {
            camera.getUsersCamera().remove(user);
        }

        userRepo.save(user);
        cameraRepo.save(camera);

        return user;
    }

    @Override
    public User updateUser(User user) {
        User userFromDb = userRepo.findByUsernameIgnoreCaseAndId(user.getUsername(), user.getId()).orElseThrow(() -> new NotFoundException("Пользователь с таким Username и Id не найден"));

        if (user.getEmail() != null && !userFromDb.getEmail().equals(user.getEmail())) {
            if (userRepo.findByEmailIgnoreCase(user.getEmail()).isPresent()) {
                throw new AlreadyExistException("Пользователь с такой почтой уже существует");
            }
        }

        userFromDb.setFirstName(user.getFirstName());
        userFromDb.setEmail(user.getEmail());

        return userRepo.save(userFromDb);
    }

    @Override
    public Page<User> getAll(Pageable pageable, String searchText) {
        Page<User> result = userPageAndSortRepo.findAllUsers(pageable, searchText.toLowerCase());

        log.info("IN getAll - {} users found", result.getTotalElements());

        return result;
    }

    @Override
    public User addRole(Long userId, String name) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        Role role = roleRepo.findByName(name)
                .orElseThrow(() -> new NotFoundException("Роль не найдена!"));

        if (!user.getRoles().contains(role)) {
            user.getRoles().add(role);
        } else {
            throw new AlreadyExistException("Пользователю уже присвоена данная роль");
        }

        userRepo.save(user);

        return user;
    }

    @Override
    public User removeRole(Long userId, String roleName) {
        User user = userRepo.findById(userId).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));
        Role role = roleRepo.findByName(roleName)
                .orElseThrow(() -> new NotFoundException("Роль не найдена!"));

        if (role.getName().equals("ROLE_USER")) {
            throw new ForbiddenException("Запрещено откреплять ROLE_USER");
        }

        if (user.getRoles().contains(role)) {
            user.getRoles().remove(role);
        } else {
            throw new AlreadyExistException("Пользователю не присвоена данная роль");
        }

        userRepo.save(user);

        return user;
    }

    @Override
    public Page<User> getAll(Pageable pageable) {
        Page<User> result = userPageAndSortRepo.findAllUsers(pageable);

        log.info("IN getAll - {} users found", result.getTotalElements());

        return result;
    }

    @Override
    public User findByUsername(String username) {

        User user = userRepo.findByUsernameIgnoreCase(username).orElseThrow(() -> new NotFoundException("Пользователь не найдена"));

        log.info("IN findById - user: {}", user);

        return user;
    }

    @Override
    public User findById(Long id) {
        User user = userRepo.findById(id).orElseThrow(() -> new NotFoundException("Пользователь не найден"));

        log.info("IN findById - user: {}", user);

        return user;
    }

    @Override
    public void delete(Long id) {
        userRepo.deleteById(id);
    }
}
