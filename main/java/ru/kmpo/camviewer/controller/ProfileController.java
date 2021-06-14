package ru.kmpo.camviewer.controller;

import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kmpo.camviewer.dto.UserDto;
import ru.kmpo.camviewer.dto.Views;
import ru.kmpo.camviewer.security.Jwt.JwtUser;
import ru.kmpo.camviewer.service.UserService;

@RestController
@RequestMapping("/api/v1/profile")
public class ProfileController {
    private UserService userService;

    public ProfileController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    @JsonView(Views.withCamera.class)
    public ResponseEntity<UserDto> getProfile(@AuthenticationPrincipal JwtUser user) {
        return new ResponseEntity<>(UserDto.fromUser(userService.findByUsername(user.getUsername())), HttpStatus.OK);
    }

}
