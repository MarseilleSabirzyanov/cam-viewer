package ru.kmpo.camviewer.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.kmpo.camviewer.domain.RoleDto;
import ru.kmpo.camviewer.repos.RoleRepo;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1/roles")
public class RoleController {
    private RoleRepo roleRepo;

    public RoleController(RoleRepo roleRepo) {
        this.roleRepo = roleRepo;
    }

    @GetMapping
    public ResponseEntity<List<RoleDto>> getAll() {
        List<RoleDto> roleDtos = roleRepo.findAll().stream()
                .map(RoleDto::fromRole)
                .collect(Collectors.toList());

        return new ResponseEntity<>(roleDtos, HttpStatus.OK);
    }
}
