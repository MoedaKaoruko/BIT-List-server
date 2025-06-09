package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.Task;
import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.service.TaskService;
import com.kf4b.bitlist.service.UserService;
import com.sun.xml.bind.v2.TODO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@RestController
@RequestMapping("/tasks")
public class TaskController {
    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;

    /**
     * 创建任务
     */
    @PostMapping("")
    public Task createTask(@RequestHeader("Authorization") String token,
                           @RequestBody Task task) {
        User user = userService.getUserByHeader(token);
        if (user == null || !Objects.equals(task.getAssignedTo(), user.getUserId())) {
            return null;
        }
        taskService.updateTaskById(-1,task);
        return task;
    }

    /**
     * 更新任务
     */
    @PutMapping("/{taskId}")
    public boolean updateTask(@RequestHeader("Authorization") String token,
                              @RequestBody Task task) {
        User user = userService.getUserByHeader(token);
        if (user == null || !Objects.equals(task.getAssignedTo(), user.getUserId())) {
            return false;
        }
        taskService.updateTaskById(task.getId(), task);
        return true;
    }

    /**
     * 删除任务
     */
    @DeleteMapping("/{taskId}")
    public boolean deleteTask(@RequestHeader("Authorization") String token,
                              @PathVariable Integer taskId) {
        User user = userService.getUserByHeader(token);
        Task task=taskService.getTaskById(taskId);
        if (user == null || !user.getUserId().equals(task.getAssignedTo())) {
            return false;
        }
        task.setDeleted(true);
        taskService.updateTaskById(taskId, task);
        return true;
    }

    /**
     * 通过用户ID获取任务列表
     */
    @GetMapping("")
    public List<Task> getTasks(@RequestHeader("Authorization") String token,
                               @RequestParam Integer userId) {
        User user = userService.getUserByHeader(token);
        if (user == null ||!user.getUserId().equals(userId)) {
            return null;
        }
        return taskService.getTaskByUserId(userId);
    }

    @PutMapping("/{taskId}/complete")
    public boolean completeTask(@RequestHeader("Authorization") String token,
                                @PathVariable Integer taskId) {
        User user = userService.getUserByHeader(token);
        Task task=taskService.getTaskById(taskId);
        if (user == null || !user.getUserId().equals(task.getAssignedTo())) {
            return false;
        }
        task.setStatus(Task.Status.DONE);
        taskService.updateTaskById(taskId, task);
        return true;
    }

    @PostMapping("/{taskId}/restore")
    public boolean restoreTask(@RequestHeader("Authorization") String token,
                               @PathVariable Integer taskId) {
        User user = userService.getUserByHeader(token);
        Task task=taskService.getTaskById(taskId);
        if (user == null || !user.getUserId().equals(task.getAssignedTo())) {
            return false;
        }
        task.setDeleted(false);
        task.setStatus(Task.Status.TODO);
        taskService.updateTaskById(taskId, task);
        return true;
    }

    @PutMapping("/reorder")
    public boolean reorderTask(@RequestHeader("Authorization") String token,
                               @RequestBody List<Task> tasks) {
        Map<Integer,Integer> orderMap=new HashMap<>();
        for (Task task : tasks) {
            orderMap.put(task.getId(),task.getOrder());
        }
        List<Task> taskList=taskService.getTaskByUserId(userService.getUserByHeader(token).getUserId());
        for (Task task : taskList) {
            if(orderMap.containsKey(task.getId())){
                task.setOrder(orderMap.get(task.getId()));
                taskService.updateTaskById(task.getId(), task);
            }
        }
        return true;
    }
}
