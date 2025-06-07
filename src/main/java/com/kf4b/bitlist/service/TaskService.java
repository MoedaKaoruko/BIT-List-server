package com.kf4b.bitlist.service;

import com.kf4b.bitlist.entity.*;
import java.util.*;

public interface TaskService {
    // 根据任务ID返回任务对象
    Task getTaskById(Integer task_id);

    // 根据用户ID返回所有相关任务列表
    List<Task> getTaskByUserId(Integer userId);

    // 更新操作，需要传入Task对象
    void updateTaskById(Integer taskId, Task task);
}
