package com.kf4b.bitlist.entity;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(generator = "uuid")
    private String id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name = "team_members", joinColumns = @JoinColumn(name = "team_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "role")
    private Map<String, Role> members = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "team_tasks", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "task_id")
    private Set<String> tasks = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "team_pending_join_requests", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "user_id")
    private Set<String> pendingJoinRequests = new HashSet<>();

    public enum Role {
        ADMIN,
        MEMBER
    }
}
