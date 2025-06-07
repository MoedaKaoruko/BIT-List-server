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
    public void updateFileBlobById(Integer FileId, FileBlob file){
        FileBlob f = fileBlobRepository.findById(FileId).get();
        f.setFileBlob(file.getFileBlob());
        f.setTaskId(file.getTaskId());
        f.setSizeInBytes(file.getSizeInBytes());
        f.setFileName(file.getFileName());
        f.getPermissions().clear();
        f.getPermissions().putAll(file.getPermissions());
        fileBlobRepository.save(f);
    }
}
