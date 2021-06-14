package ru.kmpo.camviewer.service;

import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;
import ru.kmpo.camviewer.domain.FileStorage;
import ru.kmpo.camviewer.security.Jwt.JwtUser;

import javax.annotation.PostConstruct;
import java.util.List;

public interface FileStorageService {

    @PostConstruct
    void init();

    String saveFile(MultipartFile file, JwtUser user);

    Resource loadFile(String fileName, String cameraname);

    List<FileStorage> getAllByCameraId(String username);
}
