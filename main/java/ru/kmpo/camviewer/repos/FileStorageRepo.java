package ru.kmpo.camviewer.repos;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.kmpo.camviewer.domain.FileStorage;

import java.util.List;
import java.util.Optional;

@Repository
public interface FileStorageRepo extends JpaRepository<FileStorage, Long> {
    List<FileStorageRepo> getAllByCameraId(Long id);
    Optional<FileStorage> getByFileNameAndCameraId(String fileName, Long id);
}
