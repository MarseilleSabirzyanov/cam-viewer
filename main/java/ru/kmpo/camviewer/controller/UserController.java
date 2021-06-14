package ru.kmpo.camviewer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kmpo.camviewer.domain.Camera;
import ru.kmpo.camviewer.domain.Role;
import ru.kmpo.camviewer.domain.User;
import ru.kmpo.camviewer.dto.UserDto;
import ru.kmpo.camviewer.dto.Views;
import ru.kmpo.camviewer.service.UserService;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/search/{searchText}")
    public ResponseEntity<Page<UserDto>> getAll(Pageable pageable, @PathVariable(name = "searchText") String searchText) {
        Page<UserDto> userDtos = userService.getAll(pageable, searchText).map(UserDto::fromUser);

        return new ResponseEntity<>(userDtos, HttpStatus.OK);


    }

    @GetMapping("/all")
    public ResponseEntity<Page<UserDto>> getAll(int pageNumber, int pageSize, String sortBy, String sortDir) {
        Page<UserDto> userDtos = userService.getAll(
                PageRequest.of(
                        pageNumber, pageSize,
                        sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending()
                )
        ).map(UserDto::fromUser);

        return new ResponseEntity<>(userDtos, HttpStatus.OK);
    }

    @PutMapping
    @JsonView(Views.withoutCamera.class)
    public ResponseEntity<UserDto> updateUser(@RequestBody User user) {
        return new ResponseEntity<>(UserDto.fromUser(userService.updateUser(user)), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    @JsonView(Views.withCamera.class)
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        return new ResponseEntity<>(UserDto.fromUser(userService.findById(id)), HttpStatus.OK);
    }

    @PutMapping("/{id}/addCamera")
    @JsonView(Views.withCamera.class)
    public ResponseEntity<UserDto> addCameraToUser(@PathVariable(name = "id") Long id, @RequestBody Camera camera) {
        User user = userService.addCameraToUser(id, camera.getId());

        return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
    }

    @PutMapping("/{id}/removeCamera")
    @JsonView(Views.withCamera.class)
    public ResponseEntity<UserDto> removeCameraFromUser(@PathVariable(name = "id") Long id, @RequestBody Camera camera) {
        User user = userService.removeCameraFromUser(id, camera.getId());

        return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
    }

    @PutMapping("/{id}/addRole")
    public ResponseEntity<UserDto> addRole(@PathVariable(name = "id") Long id, @RequestBody Role role) {
        User user = userService.addRole(id, role.getName());

        return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
    }

    @PutMapping("/{id}/removeRole")
    public ResponseEntity<UserDto> removeRole(@PathVariable(name = "id") Long id, @RequestBody Role role) {
        User user = userService.removeRole(id, role.getName());

        return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
    }
}
