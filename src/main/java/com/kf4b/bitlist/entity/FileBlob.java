package com.kf4b.bitlist.entity;

import javax.persistence.*;
import java.sql.Blob;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "file")
public class FileBlob {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer file_id;

    @Column(name="file_name", nullable = false,length = 255)
    private String filename;

    @Column(name="size_in_bytes")
    private Integer sizeInBytes;

    @Column(name="is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name="filedata", columnDefinition = "LONGBLOB")
    private byte[] filedata;

    @Column(name="task_id", nullable = false)
    private Integer taskId;

    @ElementCollection
    @CollectionTable(name = "file_permissions", joinColumns = @JoinColumn(name = "file_id"))
    @MapKeyColumn(name = "user_id")
    @Enumerated(EnumType.STRING)
    @Column(name = "permission")
    private Map<Integer, FileBlob.Permission> permissions = new HashMap<>();

    public enum Permission {
        PERMITTED,
        PROHIBITED
    }

    public Integer getId() {
        return file_id;
    }

    public void setId(Integer id) {
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

    public byte[] getFileBlob() {
        return filedata;
    }

    public void setFileBlob(byte[] data) {
        this.filedata = data;
    }

    public Integer getTaskId() { return this.taskId; }

    public void setTaskId(Integer taskId) { this.taskId = taskId; }

    public Map<Integer, FileBlob.Permission> getPermissions() { return this.permissions; }

    public void setPermissions(Map<Integer, FileBlob.Permission> permissions) { this.permissions = permissions; }

}
