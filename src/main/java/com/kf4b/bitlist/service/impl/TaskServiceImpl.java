package com.kf4b.bitlist.service.impl;

import com.kf4b.bitlist.entity.*;
import com.kf4b.bitlist.repository.TaskRepository;
import com.kf4b.bitlist.service.TaskService;
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
public class TaskServiceImpl implements TaskService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TaskRepository taskRepository;

    @Transactional
    public Task getTaskById(Integer task_id) {
        Optional<Task> a = taskRepository.findById(task_id);
        if(a.isPresent()){
            Task x = a.get();
            Hibernate.initialize(x.getTags());
            return x;
        }
        return null;
    }

    @Transactional
    public List<Task> getTaskByUserId(Integer userId){
        List<Task> list = taskRepository.findByAssignedTo(userId);
        if(list.isEmpty()){
            return null;
        }
        List<Task> ans = new ArrayList<>();
        Iterator<Task> iter = list.iterator();
        while(iter.hasNext()) {
            Task mid = iter.next();
            Task x = getTaskById(mid.getId());
            ans.add(x);
        }
        return ans;
    }

    @Transactional
    public void updateTaskById(Integer taskId, Task task){
        Optional<Task> xs = taskRepository.findById(taskId);
        Task x = xs.isPresent() ? xs.get() : new Task();
        x.setDeleted(task.isDeleted());
        x.setOrder(task.getOrder());
        x.setAssignedTo(task.getAssignedTo());
        x.setDescription(task.getDescription());
        x.setPriority(task.getPriority());
        x.setStatus(task.getStatus());
        x.setDueDate(task.getDueDate());
        x.setTitle(task.getTitle());
        x.getTags().clear();
        x.getTags().addAll(task.getTags());
        x.setParentTaskId(task.getParentTaskId());
        taskRepository.save(x);
    }
}
