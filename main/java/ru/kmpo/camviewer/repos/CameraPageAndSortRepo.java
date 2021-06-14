package ru.kmpo.camviewer.repos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.kmpo.camviewer.domain.Camera;

@Repository
public interface CameraPageAndSortRepo extends PagingAndSortingRepository<Camera, Long> {

    Page<Camera> findAll(Pageable pageable);
}
