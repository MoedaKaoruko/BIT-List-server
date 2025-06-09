package com.kf4b.bitlist.repository;

import com.kf4b.bitlist.entity.Team;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TeamRepository extends JpaRepository<Team, Integer> {
    // 根据用户ID查找所在团队
    @Query("SELECT DISTINCT t FROM Team t JOIN t.members m WHERE KEY(m) = :userId")
    List<Team> findByMemberUserId(@Param("userId") Integer userId);
}
