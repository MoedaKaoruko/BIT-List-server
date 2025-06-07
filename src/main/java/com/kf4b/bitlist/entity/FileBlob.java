package com.kf4b.bitlist.entity;

import javax.persistence.*;
import java.sql.Blob;

@Entity
@Table(name = "file")
public class FileBlob {
    @Id
    @GeneratedValue(generator = "uuid")
    private String file_id;

    @Column(name="filename", nullable = false,length = 255)
    private String filename;

    @Column(name="size_in_bytes")
    private Integer sizeInBytes;

    @Column(name="is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name="filedata")
    private Blob filedata;

    @Column(name="task_id", nullable = false)
    private String task_id;

    public String getId() {
        return file_id;
    }

    public void setId(String id) {
        this.file_id = id;
    }

    public String getFileName() {
        return filename;
    }

    public void setFileName(String fileName) {
        this.filename = fileName;
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

    public String getTaskId() { return this.task_id; }

    public void setTaskId(String taskId) { this.task_id = taskId; }

}
