package com.kf4b.bitlist.service.impl;

import com.kf4b.bitlist.entity.*;
import com.kf4b.bitlist.repository.TeamRepository;
import com.kf4b.bitlist.repository.TeamRepository;
import com.kf4b.bitlist.service.TeamService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import java.util.*;

@Service
public class TeamServiceImpl implements TeamService {
    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private TeamRepository teamRepository;

    @Transactional
    public Team getTeamById(Integer teamId){
        Optional<Team> t = teamRepository.findById(teamId);
        if(t.isPresent()){
            Team team = t.get();
            Hibernate.initialize(team.getMembers());
            Hibernate.initialize(team.getTasks());
            Hibernate.initialize(team.getPendingJoinRequests());
            return team;
        }
        return null;
    }

    @Transactional
    public void updateTeamById(Integer teamId, Team team){
        Optional<Team> ts = teamRepository.findById(teamId);
        Team t = ts.isPresent() ? ts.get() : new Team();
        t.setName(team.getName());
        t.setDescription(team.getDescription());

        Map<Integer, Team.Role> members = new HashMap<>(team.getMembers());
        t.getMembers().clear();
        t.getMembers().putAll(members);

        Set<Integer> tasks = new HashSet<>(team.getTasks());
        t.getTasks().clear();
        t.getTasks().addAll(tasks);

        Set<Integer> pendingJoinRequests = new HashSet<>(team.getPendingJoinRequests());
        t.getPendingJoinRequests().clear();
        t.getPendingJoinRequests().addAll(pendingJoinRequests);
        teamRepository.save(t);


    }

    @Override
    @Transactional
    public List<Team> getTeamsByUserId(Integer userId) {
        List<Team> list = teamRepository.findByMemberUserId(userId);
        if(list.isEmpty()){
            return null;
        }
        for (Team team : list) {
            Hibernate.initialize(team.getMembers());
            Hibernate.initialize(team.getTasks());
            Hibernate.initialize(team.getPendingJoinRequests());
        }
        return list;
    }

    @Transactional
    public void deleteTeam(Integer teamId) {
        teamRepository.deleteById(teamId);
    }
}
