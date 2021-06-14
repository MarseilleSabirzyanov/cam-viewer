package ru.kmpo.camviewer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.kmpo.camviewer.domain.User;
import ru.kmpo.camviewer.dto.UserDto;
import ru.kmpo.camviewer.service.UserService;

@RestController
@RequestMapping("/api/v1/register")
public class RegistrationController {
    private UserService userService;

    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<UserDto> register(@RequestBody User user) {
        User registeredUser = userService.register(user);
        return new ResponseEntity<>(UserDto.fromUser(registeredUser), HttpStatus.OK);
    }
}
