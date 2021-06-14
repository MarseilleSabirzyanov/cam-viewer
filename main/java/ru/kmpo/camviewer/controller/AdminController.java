package ru.kmpo.camviewer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kmpo.camviewer.domain.User;
import ru.kmpo.camviewer.dto.UserDto;
import ru.kmpo.camviewer.service.UserService;

@RestController
@RequestMapping("/api/v1/admin")
public class AdminController {
    private final UserService userService;

    public AdminController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public String hello() {
        return "hello";
    }

    @GetMapping(value = "user/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable(name = "id") Long id) {
        User user = userService.findById(id);

        return new ResponseEntity<>(UserDto.fromUser(user), HttpStatus.OK);
    }


}
