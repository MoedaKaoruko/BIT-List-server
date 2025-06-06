package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.Task;
import com.kf4b.bitlist.entity.User;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @PostMapping("")
    public Map<String, Object> createTask(@RequestBody Task task) {
        // TODO: Implement task creation logic
        Map<String, Object> result = new HashMap<>();
        result.put("taskId", 123);
        return result;
    }

    @PutMapping("/{taskId}")
    public boolean updateTask(@RequestBody Task task) {
        // TODO: Implement task update logic
        return true;
    }

    @DeleteMapping("/{taskId}")
    public boolean deleteTask(Long taskId) {
        // TODO: Implement task deletion logic
        return true;
    }

    @GetMapping("")
    public Map<String, Object> getTasks(String taskId) {
        // TODO: Implement task retrieval logic
        Map<String, Object> result = new HashMap<>();
        result.put("tasks", "tasks");
        return result;
    }


}
