package com.kf4b.bitlist;

import com.kf4b.bitlist.repository.*;
import com.kf4b.bitlist.service.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.kf4b.bitlist.entity.*;
import org.springframework.jdbc.core.JdbcTemplate;
import org.hibernate.engine.jdbc.BlobProxy;
import java.util.*;
import java.nio.file.Files;
import java.io.File;
import java.io.IOException;


@SpringBootTest
class BitListApplicationTests {
    public byte[] convertFileToBytes(File file) throws IOException {
        return Files.readAllBytes(file.toPath());
    }

    @Autowired
    private TeamService teamService;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private TaskService taskService;

    @Autowired
    private UserService userService;


    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void contextLoads() throws Exception {

        Map<Integer, Team.Role> m1 = new HashMap<>();
        m1.put(1, Team.Role.ADMIN);
        m1.put(2, Team.Role.MEMBER);
        m1.put(3, Team.Role.MEMBER);
        m1.put(4, Team.Role.MEMBER);
//
//        Map<Integer, Team.Role> m2 = new HashMap<>();
//        m2.put(2, Team.Role.MEMBER);
//        m2.put(4, Team.Role.ADMIN);
//        m2.put(5, Team.Role.MEMBER);
//
        Set<Integer> s1 = new HashSet<>();
        s1.add(1);
        s1.add(3);
        s1.add(4);
//
//        Set<Integer> s2 = new HashSet<>();
//        s2.add(1);
//        s2.add(4);
//        s2.add(6);
//
        Set<Integer> r1 = new HashSet<>();
        r1.add(5);
//
//        Set<Integer> r2 = new HashSet<>();
//        r2.add(1);
//        r2.add(3);

        Team t = new Team();
        t.setId(8);
        t.setName("team1");
        t.setDescription("description_new");
        t.setTasks(s1);
        t.setMembers(m1);
        t.setPendingJoinRequests(r1);
        Team team = teamService.getTeamById(t.getId());
        System.out.println(team.getMembers());
        System.out.println(team.getTasks());
        System.out.println(team.getPendingJoinRequests());
        teamService.updateTeamById(t.getId(), t);
        team = teamService.getTeamById(t.getId());
        System.out.println(team.getMembers());
        System.out.println(team.getTasks());
        System.out.println(team.getPendingJoinRequests());






    }

}
