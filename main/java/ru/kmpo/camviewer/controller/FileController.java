package ru.kmpo.camviewer.controller;

import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import ru.kmpo.camviewer.file.FileResponse;
import ru.kmpo.camviewer.security.Jwt.JwtUser;
import ru.kmpo.camviewer.service.FileStorageService;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/v1camera/file")
public class FileController {
    private FileStorageService storageService;

    public FileController(FileStorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/uploadfile")
    public ResponseEntity<FileResponse> uploadSingleFile (@RequestParam("file") MultipartFile file,
                                                          @AuthenticationPrincipal JwtUser user) {
        String upfile = storageService.saveFile(file, user);

        String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/camera/download/" + user.getUsername() + "/")
                .path(upfile)
                .toUriString();

        return ResponseEntity.status(HttpStatus.OK).body(new FileResponse(upfile, fileDownloadUri, "Файл успешно загружен!"));
    }

    @PostMapping("/uploadfiles")
    public ResponseEntity<List<FileResponse>> uploadMultipleFiles (@RequestParam("files") MultipartFile[] files,
                                                                   @AuthenticationPrincipal JwtUser user) {

        List<FileResponse> responses = Arrays.stream(files)
                .map(
                        file -> {
                            String upfile = storageService.saveFile(file, user);
                            String fileDownloadUri = ServletUriComponentsBuilder.fromCurrentContextPath()
                                    .path("/camera/download/")
                                    .path(upfile)
                                    .toUriString();
                            return new FileResponse(upfile,fileDownloadUri,"Файл успешно загружен!");
                        }
                )
                .collect(Collectors.toList());
        return ResponseEntity.status(HttpStatus.OK).body(responses);
    }

    @GetMapping("/download/{cameraname}/{filename:.+}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename, @PathVariable String cameraname) {

        Resource resource = storageService.loadFile(filename, cameraname);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + resource.getFilename() + "\"")
                .body(resource);
    }
}
