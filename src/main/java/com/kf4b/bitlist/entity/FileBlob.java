package com.kf4b.bitlist.entity;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "fileblob")
public class FileBlob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer fileId;

    @Column(nullable = false,length = 255)
    private String fileName;

    private Integer sizeInBytes;

    private boolean isDeleted = false;

    private Blob filedata;

    private Integer taskId;

    public Integer getId() {
        return fileId;
    }

    public void setId(Integer id) {
        this.fileId = id;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Integer getSizeInBytes() {
        return sizeInBytes;
    }

    public void setSizeInBytes(Integer sizeInBytes) {
        this.sizeInBytes = sizeInBytes;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Blob getAttachmentLink() {
        return filedata;
    }

    public void setAttachmentLink(Blob data) {
        this.filedata = data;
    }

    public Integer getTaskId() { return this.taskId; }

    public void setTaskId(Integer taskId) { this.taskId = taskId; }

}
