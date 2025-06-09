package com.kf4b.bitlist.repository;

import com.kf4b.bitlist.entity.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.*;


public interface TaskRepository extends JpaRepository<Task, Integer> {

    // 根据用户ID搜索所有相关任务（包括已完成和未完成，所有已完成的任务可通过save方法恢复为未完成状态）
    List<Task> findByAssignedTo(Integer assignedTo);


}
