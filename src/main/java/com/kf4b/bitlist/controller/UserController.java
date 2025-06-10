package com.kf4b.bitlist.controller;

import com.kf4b.bitlist.entity.MindedItem;
import com.kf4b.bitlist.entity.Task;
import com.kf4b.bitlist.entity.Team;
import com.kf4b.bitlist.entity.User;
import com.kf4b.bitlist.service.MindedItemService;
import com.kf4b.bitlist.service.TaskService;
import com.kf4b.bitlist.service.TeamService;
import com.kf4b.bitlist.service.UserService;
import com.kf4b.bitlist.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.swing.text.DateFormatter;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private MindedItemService mindedItemService;

    @Autowired
    private TaskService taskService;

    @Autowired
    private TeamService teamService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    /**
     * 获取用户信息
     */
    @GetMapping("/profile")
    public User getUserProfile(@RequestHeader("Authorization") String token) {
        return userService.getUserByHeader(token);
    }

    /**
     * 更新用户信息
     */
    @PutMapping("/update")
    public boolean updateUserProfile(@RequestHeader("Authorization") String token,
                                     @RequestBody Map<String, Object> requestMap) {
        User user = userService.getUserByHeader(token);
        if(!user.getUserId().equals(requestMap.get("userId"))){
            return false;
        }
        if(requestMap.containsKey("username")) {
            user.setUsername((String) requestMap.get("username"));
        }
        if(requestMap.containsKey("email")) {
            user.setEmail((String) requestMap.get("email"));
        }
        if(requestMap.containsKey("grade")) {
            //user.set((String) requestMap.get("grade"));
        }
        if(requestMap.containsKey("birth")) {
            user.setBirth((Date) requestMap.get("birth"));
        }
        if(requestMap.containsKey("stuId")) {
            //user.setStuId((String) requestMap.get("stuId"));
        }
        if(requestMap.containsKey("avatarUri")) {
            user.setAvatarUri((String) requestMap.get("avatarUri"));
        }
        userService.updateUserById(user.getUserId(), user);
        return true;
    }

    /**
     * 更改密码
     */
    @PutMapping("/changePassword")
    public boolean changePassword(@RequestHeader("Authorization") String token,
                                  @RequestBody Map<String, Object> requestMap) {
        User user = userService.getUserByHeader(token);
        if(!user.getUserId().equals(requestMap.get("userId"))){
            return false;
        }
        if(requestMap.containsKey("oldPassword") && requestMap.containsKey("newPassword")) {
            if(!user.getPassword().equals(requestMap.get("oldPassword"))){
                return false;
            }
            user.setPassword((String) requestMap.get("newPassword"));
        }
        return true;
    }

    @GetMapping("/stats")
    public Map<String, Object> getUserStats(@RequestHeader("Authorization") String token,
                                            @RequestParam Integer userId,
                                            @RequestParam Integer trendLength,
                                            @RequestParam Integer taskDateRange,
                                            @RequestParam String timeAllocationPeriod
    ) {
        User user = userService.getUserByHeader(token);
        Map<String, Object> result = new HashMap<>();
        if(!user.getUserId().equals(userId)){
            result.put("message", "validation error");
            result.put("success", "false");
            return result;
        }
        int completedTasks = 0;
        int inProgressTasks = 0;
        int totalTeams = 0;
        int totalFocusTime = 0;
        List<Map<String,Object>> completionRateTrend = new ArrayList<>();
        Map<String,Object> recentTaskSummary = new HashMap<>();
        List<Map<String,Object>> timeAllocationReport = new ArrayList<>();

        LocalDate today = LocalDate.now();




        // 获取任务信息
        List<Task> taskList = taskService.getTaskByUserId(userId);
        completedTasks = (int) taskList.stream()
              .filter(t -> t.getStatus() == Task.Status.DONE)
              .count();
        inProgressTasks = (int) taskList.stream()
              .filter(t -> t.getStatus() == Task.Status.IN_PROGRESS)
              .count();

        // 新增：获取专注时间信息
        LocalDate currentWeekStart = today.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        for (int i = 0; i < trendLength; i++) {
            LocalDate weekStart = currentWeekStart.minusWeeks(i);
            LocalDate weekEnd = weekStart.plusWeeks(1).minusDays(1);

            // 筛选本周内截止的任务（根据业务需求可替换为createdDate）
            List<Task> weeklyTasks = taskList.stream()
                    .filter(task -> {
                        LocalDate dueDate = task.getDueDate().toInstant()
                                .atZone(java.time.ZoneId.systemDefault())
                                .toLocalDate();
                        return !dueDate.isBefore(weekStart) && !dueDate.isAfter(weekEnd);
                    })
                    .collect(Collectors.toList());

            long completed = weeklyTasks.stream()
                    .filter(t -> t.getStatus() == Task.Status.DONE)
                    .count();
            float rate = weeklyTasks.isEmpty() ? 0 : (float) completed / weeklyTasks.size();

            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("period", "W" + String.valueOf(i + 1));  // 最近一周为W1，依次往前
            dataPoint.put("rate", rate);
            completionRateTrend.add(dataPoint);
        }

        // 新增：最近taskDateRange天任务状态统计
        LocalDate taskStartDate = today.minusMonths(taskDateRange);
        LocalDate taskEndDate = today;
        List<Task> recentTasks = taskList.stream()
                .filter(task -> {
                    LocalDate taskDate = task.getDueDate().toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate();
                    return !taskDate.isBefore(taskStartDate) && !taskDate.isAfter(taskEndDate);
                })
                .collect(Collectors.toList());

        long todo = recentTasks.stream()
                .filter(t -> t.getStatus() == Task.Status.TODO)
                .count();
        long inProgress = recentTasks.stream()
                .filter(t -> t.getStatus() == Task.Status.IN_PROGRESS)
                .count();
        long completed = recentTasks.stream()
                .filter(t -> t.getStatus() == Task.Status.DONE)
                .count();

        recentTaskSummary.put("todo", todo);
        recentTaskSummary.put("inProgress", inProgress);
        recentTaskSummary.put("completed", completed);

        // 新增：时间分配报告
        LocalDate timeEnd = LocalDate.now();
        LocalDate timeStart;

        // 1. 根据周期确定时间范围
        if ("daily".equals(timeAllocationPeriod)) {
            timeStart = timeEnd;  // 当天（今日）
        } else if ("weekly".equals(timeAllocationPeriod)) {
            // 当周（周一到周日）
            timeStart = timeEnd.with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));
        } else {
            // 默认处理：最近7天
            timeStart = timeEnd.minusDays(7);
        }

        // 2. 筛选时间范围内的任务（假设使用任务创建时间）
        List<Task> timeRangeTasks = taskList.stream()
                .filter(task -> {
                    // 将Task的Date类型创建时间转为LocalDate
                    LocalDate taskCreateDate = task.getDueDate().toInstant()
                            .atZone(java.time.ZoneId.systemDefault())
                            .toLocalDate();
                    return !taskCreateDate.isBefore(timeStart) && !taskCreateDate.isAfter(timeEnd);
                })
                .collect(Collectors.toList());

        // 3. 统计每个标签的任务数量（假设Task有getTags()返回标签列表）
        Map<String, Integer> tagCountMap = new HashMap<>();
        for (Task task : timeRangeTasks) {
            List<String> tags = task.getTags();  // 需确保Task实体有此方法
            for (String tag : tags) {
                tagCountMap.put(tag, tagCountMap.getOrDefault(tag, 0) + 1);
            }
        }

        // 4. 计算标签占比并生成报告
        int totalTaskCount = timeRangeTasks.size();
        for (Map.Entry<String, Integer> entry : tagCountMap.entrySet()) {
            String tag = entry.getKey();
            int count = entry.getValue();
            float percentage = totalTaskCount == 0 ? 0 : (float) count / totalTaskCount;

            Map<String, Object> dataPoint = new HashMap<>();
            dataPoint.put("category", tag);  // 标签名
            dataPoint.put("percentage", percentage);  // 占比
            timeAllocationReport.add(dataPoint);
        }


        // 获取团队信息
        List<Team> teamList = teamService.getTeamsByUserId(userId);
        if (teamList!=null){
            totalTeams = teamList.size();
        }

        // 获取专注时间信息
        totalFocusTime = mindedItemService.totalDurationInSeconds(userId);




        // 构建返回结果
        result.put("completedTasks", completedTasks);
        result.put("inProgressTasks", inProgressTasks);
        result.put("totalTeams", totalTeams);
        result.put("totalFocusTime", totalFocusTime);
        result.put("success", "true");
        result.put("completionRateTrend", completionRateTrend);
        result.put("recentTaskSummary", recentTaskSummary);
        result.put("timeAllocationReport", timeAllocationReport);
        return result;
    }

    @PostMapping("/focus")
    public boolean getUserMindedItem(@RequestHeader("Authorization") String token,
                                     @RequestBody MindedItem mindedItem) {
        User user = userService.getUserByHeader(token);
        if(!user.getUserId().equals(mindedItem.getUserId())){
            return false;
        }
        mindedItemService.updateMindedItemById(-1, mindedItem);
        return true;
    }

}