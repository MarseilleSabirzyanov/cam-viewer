package ru.kmpo.camviewer.domain;

import lombok.Data;

import javax.persistence.*;

@Entity
@Table(name = "file_storage")
@Data
public class FileStorage extends BaseEntity{
    @Column(name = "file_name")
    private String fileName;

    @ManyToOne
    @JoinColumn(name = "camera_id", nullable = false)
    private Camera camera;

    @Column(name = "document_extension")
    private String documentExtension;

    @Column(name = "upload_dir")
    private String uploadDir;
}
