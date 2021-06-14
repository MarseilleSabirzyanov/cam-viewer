package ru.kmpo.camviewer.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.kmpo.camviewer.domain.Role;

import java.util.Optional;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}
