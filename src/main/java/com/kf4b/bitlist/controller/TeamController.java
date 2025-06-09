package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.Team;
import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.service.TeamService;
import com.kf4b.bitlist.service.UserService;
import com.kf4b.bitlist.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/teams")
public class TeamController {
    @Autowired
    private TeamService teamService;

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    private boolean checkPermissionFailure(User user, Team team, Team.Role role) {
        if (user == null || team == null) {
            return true;
        }
        Team.Role userRole = team.getMembers().get(user.getUserId());
        if(role == Team.Role.ADMIN){
            return userRole == null || !userRole.equals(Team.Role.ADMIN);
        }else{
            return userRole == null || (!userRole.equals(Team.Role.ADMIN) && !userRole.equals(Team.Role.MEMBER));
        }
    }


    /**
     * 创建团队
     * @param team 团队对象
     * @return 新创建的团队对象
     */
    @PostMapping("")
    public Team createTeam(@RequestHeader("Authorization") String token,
                           @RequestBody Team team) {
        // 检查token
        User user = userService.getUserByHeader(token);
        if (user == null) {
            return null;
        }
        // 将当前用户作为管理员加入团队
        Map<Integer, Team.Role> members = team.getMembers();
        members.put(user.getUserId(), Team.Role.ADMIN);
        // 保存团队
        teamService.updateTeamById(-1,team);
        return team;
    }

    /**
     * 获取团队信息
     * @param userId 团队ID
     * @return 更新后的团队对象
     */
    @GetMapping("")
    public List<Team> getTeamById(@RequestHeader("Authorization") String token,
                            @RequestParam Integer userId) {
        // 检查token
        User user = userService.getUserByHeader(token);
        if (user == null || !user.getUserId().equals(userId)) {
            return null;
        }
        return teamService.getTeamsByUserId(userId);
    }

    /**
     * 更新团队信息
     * @param teamId 团队ID
     * @param team 要更新的团队对象
     * @return 更新后的团队对象
     */
    @PutMapping("/{teamId}")
    public Team updateTeam(@RequestHeader("Authorization") String token,
                           @PathVariable Integer teamId,
                           @RequestBody Team team) {
        // 检查token
        User user = userService.getUserByHeader(token);
        Team teamToUpdate = teamService.getTeamById(teamId);
        if(checkPermissionFailure(user, teamToUpdate, Team.Role.ADMIN)){
            return null;
        }
        // 更新
        team.setId(teamId);
        teamService.updateTeamById(teamId, team);
        return team;
    }

    /**
     * 删除团队
     * @param teamId 团队ID
     */
    @DeleteMapping("{teamId}")
    public boolean deleteTeam(@RequestHeader("Authorization") String token,
                              @PathVariable Integer teamId) {
        // 检查token
        User user = userService.getUserByHeader(token);
        Team team = teamService.getTeamById(teamId);
        if(checkPermissionFailure(user, team, Team.Role.ADMIN)){
            return false;
        }
        // 删除
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
        Team team = teamService.getTeamById(teamId);
        if(checkPermissionFailure(user, team, Team.Role.MEMBER)){
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
        Team team = teamService.getTeamById(teamId);
        // 检查用户是否是团队的成员
        if (team.getMembers().containsKey(user.getUserId())) {
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
        Team team = teamService.getTeamById(teamId);
        // 检查用户是否是团队的管理员
        if(checkPermissionFailure(user, team, Team.Role.ADMIN)){
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
        Team team = teamService.getTeamById(teamId);
        // 检查用户是否是团队的管理员
        if(checkPermissionFailure(user, team, Team.Role.ADMIN)){
            return false;
        }
        // 检查用户是否存在
        if (userService.getUserById(userIdToUpdate) == null) {
            return false;
        }
        // 添加/更新成员角色
        team.getMembers().put(userIdToUpdate, role);
        teamService.updateTeamById(teamId, team);
        return true;
    }
}
