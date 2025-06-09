package com.kf4b.bitlist.service;

import com.kf4b.bitlist.entity.*;
import java.util.*;

public interface TeamService {

    // 通过主键获取团队信息
    Team getTeamById(Integer teamId);

    // 更新操作
    void updateTeamById(Integer teamId, Team team);

    // 根据用户ID查找所在团队列表
    List<Team> getTeamsByUserId(Integer userId);

    // 删除操作
    void deleteTeam(Integer teamId);

}
