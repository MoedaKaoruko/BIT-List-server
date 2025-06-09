package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.Task;
import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.service.TaskService;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    /**
     * 创建任务
     */
    @PostMapping("")
    public Task createTask(@RequestBody Task task) {
        taskService.updateTaskById(-1,task);
        return task;
    }

    /**
     * 更新任务
     */
    @PutMapping("/{taskId}")
    public boolean updateTask(@RequestBody Task task) {
        taskService.updateTaskById(task.getId(), task);
        return true;
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{taskId}")
    public boolean deleteTask(@PathVariable Integer taskId) {
        Task task=taskService.getTaskById(taskId);
        task.setDeleted(true);
        taskService.updateTaskById(taskId, task);
        return true;
    }

    /**
     * 通过用户ID获取任务列表
     */
    @GetMapping("")
    public List<Task> getTasks(@RequestParam Integer userId) {
        return taskService.getTaskByUserId(userId);
    }

    @PutMapping("/{taskId}/complete")
    public boolean completeTask(@PathVariable Integer taskId) {
        Task task=taskService.getTaskById(taskId);
        task.setStatus(Task.Status.DONE);
        taskService.updateTaskById(taskId, task);
        return true;
    }

    @PostMapping("/{taskId}/restore")
    public boolean restoreTask(@PathVariable Integer taskId) {
        Task task=taskService.getTaskById(taskId);
        task.setDeleted(false);
        task.setStatus(Task.Status.TODO);
        taskService.updateTaskById(taskId, task);
        return true;
    }

    @PutMapping("/{taskId}/reorder")
    public boolean reorderTask(@PathVariable Integer taskId,
                               @RequestBody Map<String, Integer> requestBody) {
        // XXX:未实现
        Integer newOrder = requestBody.get("order");
        Task task=taskService.getTaskById(taskId);
        task.setOrder(newOrder);
        taskService.updateTaskById(taskId, task);
        return true;
    }
}
