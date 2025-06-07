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
    private String team_id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "description", nullable = false)
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

    public String getId() { return this.team_id; }

    public void setId(String id) { this.team_id = id; }

    public String getName() { return this.name; }

    public void setName(String name) { this.name = name; }

    public String getDescription() { return this.description; }

    public void setDescription(String description) { this.description = description; }

    public Map<String, Role> getMembers() { return this.members; }

    public void setMembers(Map<String, String> members){
        for (Map.Entry<String, String> entry : members.entrySet()) {
            String value = entry.getValue();
            // 仅接受完全匹配的枚举名称
            if ("ADMIN".equals(value) || "MEMBER".equals(value)) {
                this.members.put(entry.getKey(), Role.valueOf(value));
            }
        }
    }

    public Set<String> getTasks() { return this.tasks; }

    public void setTasks(Set<String> tasks) {
        this.tasks = tasks != null ? new HashSet<>(tasks) : new HashSet<>();
    }

    public Set<String> getPendingJoinRequests() { return this.pendingJoinRequests; }

    public void setPendingJoinRequests(Set<String> pendingJoinRequests) {
        this.pendingJoinRequests = pendingJoinRequests != null ? new HashSet<>(pendingJoinRequests) : new HashSet<>();
    }
}
