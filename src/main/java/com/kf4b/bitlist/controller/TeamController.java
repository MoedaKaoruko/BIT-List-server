package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.Team;
import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.service.TeamService;
import com.kf4b.bitlist.service.UserService;
import com.kf4b.bitlist.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/teams")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;


    /**
     * 创建团队
     * @param team 团队对象
     * @return 新创建的团队对象
     */
    @PostMapping("")
    public Team createTeam(@RequestBody Team team) {
        teamService.updateTeamById(-1,team);
        return team;
    }

    /**
     * 更新团队信息
     * @param teamId 团队ID
     * @return 更新后的团队对象
     */
    @GetMapping("")
    public Team getTeamById(@RequestParam Integer teamId) {
        return teamService.getTeamById(teamId);
    }

    /**
     * 更新团队信息
     * @param teamId 团队ID
     * @param team 要更新的团队对象
     * @return 更新后的团队对象
     */
    @PutMapping("{teamId}")
    public Team updateTeam(@PathVariable Integer teamId, @RequestBody Team team) {
        team.setId(teamId);
        teamService.updateTeamById(teamId, team);
        return team;
    }

    /**
     * 删除团队
     * @param teamId 团队ID
     */
    @DeleteMapping("{teamId}")
    public boolean deleteTeam(@PathVariable Integer teamId) {
        teamService.deleteTeam(teamId);
        return true;
    }

    /**
     * 分配任务给团队
     * @param teamId 团队ID
     * @param taskId 任务ID
     */
    @PostMapping("/{teamId}/tasks/{taskId}/assign")
    public boolean assignTask(@PathVariable Integer teamId, @PathVariable Integer taskId,
                              @RequestParam Integer userId,
                              @RequestHeader("Authorization") String token) {
        // 检查token
        User user = userService.getUserByHeader(token);
        if (user == null || !user.getUserId().equals(userId)) {
            return false;
        }
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            return false;
        }
        // 检查用户是否是团队的成员
        if (!team.getMembers().containsKey(user.getUserId())) {
            return false;
        }
        team.getTasks().add(taskId);
        teamService.updateTeamById(teamId, team);
        return true;
    }

    /**
     * 发送加入请求
     */
    @PostMapping("/{teamId}/join")
    public boolean sendJoinRequest(@PathVariable Integer teamId,
                                   @RequestParam Integer userId,
                                   @RequestHeader("Authorization") String token) {
        // 检查token
        User user = userService.getUserByHeader(token);
        if (user == null || !user.getUserId().equals(userId)) {
            return false;
        }
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            return false;
        }
        team.getPendingJoinRequests().add(user.getUserId());
        teamService.updateTeamById(teamId, team);
        return true;
    }

    /**
     * 移除团队成员
     */
    @DeleteMapping("/{teamId}/members/{userId}")
    public boolean removeMember(@PathVariable Integer teamId, @PathVariable Integer userIdToRemove,
                                @RequestHeader("Authorization") String token) {
        // 检查token
        User user = userService.getUserByHeader(token);
        if (user == null) {
            return false;
        }
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            return false;
        }
        // 检查用户是否是团队的管理员
        if (!team.getMembers().get(user.getUserId()).equals(Team.Role.ADMIN)) {
            return false;
        }
        // 检查用户是否存在
        if (userService.getUserById(userIdToRemove) == null) {
            return false;
        }
        team.getMembers().remove(userIdToRemove);
        teamService.updateTeamById(teamId, team);
        return true;
    }

    /**
     * 更新/添加成员角色
     */
    @PutMapping("/{teamId}/members/{userId}/role")
    public boolean updateMemberRole(@PathVariable Integer teamId, @PathVariable Integer userIdToUpdate,
                                    @RequestParam Team.Role role,
                                    @RequestHeader("Authorization") String token) {
        // 检查token
        User user = userService.getUserByHeader(token);
        if (user == null) {
            return false;
        }
        Team team = teamService.getTeamById(teamId);
        if (team == null) {
            return false;
        }
        // 检查用户是否是团队的管理员
        if (!team.getMembers().get(user.getUserId()).equals(Team.Role.ADMIN)) {
            return false;
        }
        // 检查用户是否存在
        if (userService.getUserById(userIdToUpdate) == null) {
            return false;
        }
        team.getMembers().put(userIdToUpdate, role);
        teamService.updateTeamById(teamId, team);
        return true;
    }
}
