package com.kf4b.bitlist.service.impl;

import com.kf4b.bitlist.entity.*;
import com.kf4b.bitlist.service.FileBlobService;
import com.kf4b.bitlist.repository.FileBlobRepository;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.sql.Blob;
import java.util.*;

@Service
public class FileBlobServiceImpl implements FileBlobService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private FileBlobRepository fileBlobRepository;

    @Transactional
    public FileBlob getFileBlobById(Integer FileId){
        Optional<FileBlob> f = fileBlobRepository.findById(FileId);
        if(f.isPresent()){
            FileBlob file = f.get();
            Hibernate.initialize(file.getPermissions());
            return file;
        }
        return null;
    }

    @Transactional
    public List<FileBlob> getFileBlobByTaskId(Integer TaskId){
        List<FileBlob> list = fileBlobRepository.findByTaskId(TaskId);
        return list.isEmpty() ? null : list;
    }

    @Transactional
    public FileBlob updateFileBlobById(Integer FileId, FileBlob file){
        Optional<FileBlob> fs = fileBlobRepository.findById(FileId);
        FileBlob f = fs.isPresent() ? fs.get() : new FileBlob();
        f.setFileBlob(file.getFileBlob());
        f.setTaskId(file.getTaskId());
        f.setSizeInBytes(file.getSizeInBytes());
        f.setFileName(file.getFileName());
        Map<Integer, FileBlob.Permission> permissions = new HashMap<>(file.getPermissions());
        f.getPermissions().clear();
        f.getPermissions().putAll(permissions);
        fileBlobRepository.save(f);
        return f;
    }

    @Transactional
    public void deleteFileBlob(Integer FileId){
        fileBlobRepository.deleteFileBlob(FileId);
    }

    @Transactional
    public void restoreFileBlob(Integer FileId){
        fileBlobRepository.restoreFileBlob(FileId);
    }
}
