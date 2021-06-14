package ru.kmpo.camviewer.service.impl;

import liquibase.util.file.FilenameUtils;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import ru.kmpo.camviewer.domain.Camera;
import ru.kmpo.camviewer.domain.FileStorage;
import ru.kmpo.camviewer.domain.Status;
import ru.kmpo.camviewer.domain.configurationProperties.FileUploadProperties;
import ru.kmpo.camviewer.exception.FileNotFoundException;
import ru.kmpo.camviewer.exception.FileStorageException;
import ru.kmpo.camviewer.exception.NotFoundException;
import ru.kmpo.camviewer.repos.CameraRepo;
import ru.kmpo.camviewer.repos.FileStorageRepo;
import ru.kmpo.camviewer.security.Jwt.JwtUser;
import ru.kmpo.camviewer.service.FileStorageService;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Date;
import java.util.List;

import static java.nio.file.Files.createDirectories;

@Service
public class FileStorageServiceImpl implements FileStorageService {
    private final Path dirLocation;
    private CameraRepo cameraRepo;
    private FileStorageRepo fileStorageRepo;

    public FileStorageServiceImpl(FileUploadProperties fileUploadProperties, CameraRepo cameraRepo, FileStorageRepo fileStorageRepo) {
        this.dirLocation = Paths.get(fileUploadProperties.getLocation())
                .toAbsolutePath()
                .normalize();
        this.cameraRepo = cameraRepo;
        this.fileStorageRepo = fileStorageRepo;
    }

    @Override
    @PostConstruct
    public void init() {
        try {
            Files.createDirectories(this.dirLocation);
        } catch (IOException e) {
            throw new FileStorageException("Ошибка при создании директории");
        }
    }

    @Override
    public String saveFile(MultipartFile file, JwtUser user) {
        try {
            Camera camera = cameraRepo.findByUserId(user.getId()).orElseThrow(() -> new NotFoundException("Пользователь не найден!"));

            //TODO add year month and day to dir
            String fileName = file.getOriginalFilename();
            Path fileDir = Paths.get(this.dirLocation.toString(), camera.getUser().getUsername())
                    .toAbsolutePath()
                    .normalize();
            Path dfile = fileDir.resolve(fileName);

            createDirectories(fileDir);
            Files.copy(file.getInputStream(), dfile, StandardCopyOption.REPLACE_EXISTING);

            FileStorage fileStorage = new FileStorage();
            fileStorage.setFileName(fileName);
            fileStorage.setCamera(camera);
            fileStorage.setDocumentExtension(FilenameUtils.getExtension(fileName));
            fileStorage.setUploadDir(fileDir.toString());
            fileStorage.setCreated(new Date());
            fileStorage.setUpdated(new Date());
            fileStorage.setStatus(Status.ACTIVE);

            fileStorageRepo.save(fileStorage);

            return fileName;
        } catch (IOException e) {
            throw new FileStorageException("Ошибка при загрузки файла!");
        }
    }

    @Override
    public Resource loadFile(String fileName, String cameraname) {
        try {
            Path file = Paths.get(this.dirLocation.toString(), cameraname)
                    .toAbsolutePath()
                    .normalize()
                    .resolve(fileName)
                    .normalize();
            Resource resource = new UrlResource(file.toUri());

            if (resource.exists() || resource.isReadable()) {
                return resource;
            } else {
                throw new FileNotFoundException("Файл не найден!");
            }
        } catch (MalformedURLException e) {
            throw new FileNotFoundException("Файл не найден!");
        }
    }

    @Override
    public List<FileStorage> getAllByCameraId(String username) {
        return null;
    }

}
