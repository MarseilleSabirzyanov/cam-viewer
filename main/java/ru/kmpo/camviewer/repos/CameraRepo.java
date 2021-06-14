package ru.kmpo.camviewer.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kmpo.camviewer.domain.Camera;

import java.util.Optional;

@Repository
public interface CameraRepo extends JpaRepository<Camera, Long> {
    Optional<Camera> findByUserId(Long id);
}
