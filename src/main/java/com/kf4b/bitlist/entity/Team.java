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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @ElementCollection
    @CollectionTable(name = "team_members", joinColumns = @JoinColumn(name = "team_id"))
    @MapKeyColumn(name = "user_id")
    @Column(name = "role")
    private Map<Integer, Role> members = new HashMap<>();

    @ElementCollection
    @CollectionTable(name = "team_tasks", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "task_id")
    private Set<Integer> tasks = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "team_pending_join_requests", joinColumns = @JoinColumn(name = "team_id"))
    @Column(name = "user_id")
    private Set<Integer> pendingJoinRequests = new HashSet<>();

    public enum Role {
        ADMIN,
        MEMBER
    }

    public Integer getId() { return this.id; }

    public void setId(Integer id) { this.id = id; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

    public Map<Integer, Role> getMembers() { return this.members; }

    public void setMembers(Map<Integer, String> members){
        for (Map.Entry<Integer, String> entry : members.entrySet()) {
            String value = entry.getValue();
            // 仅接受完全匹配的枚举名称
            if ("ADMIN".equals(value) || "MEMBER".equals(value)) {
                this.members.put(entry.getKey(), Role.valueOf(value));
            }
        }
    }

    public Set<Integer> getTasks() { return this.tasks; }

    public void setTasks(Set<Integer> tasks) {
        this.tasks = tasks != null ? new HashSet<>(tasks) : new HashSet<>();
    }

    public Set<Integer> getPendingJoinRequests() { return this.pendingJoinRequests; }

    public void setPendingJoinRequests(Set<Integer> pendingJoinRequests) {
        this.pendingJoinRequests = pendingJoinRequests != null ? new HashSet<>(pendingJoinRequests) : new HashSet<>();
    }
}
