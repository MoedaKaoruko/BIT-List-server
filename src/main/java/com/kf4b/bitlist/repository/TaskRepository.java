package com.kf4b.bitlist.repository;

import com.kf4b.bitlist.entity.Task;
import org.springframework.data.jpa.repository.JpaRepository;


public interface TaskRepository extends JpaRepository<Task, Integer> {
}
