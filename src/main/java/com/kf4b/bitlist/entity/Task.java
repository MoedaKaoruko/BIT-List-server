package com.kf4b.bitlist.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kf4b.bitlist.converter.JsonListConverter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "task")
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer task_id;

    @Column(name = "title", nullable = false)
    private String title;

    @Column(name = "description", nullable = false)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "priority", nullable = false)
    private Priority priority;

    //额外标签字段
    @ElementCollection
    @CollectionTable(name = "task_tags", joinColumns = @JoinColumn(name = "task_id"))
    @Column(name = "tag")
    private List<String> tags = new ArrayList<>();

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status;

    @Column(name = "due_date", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date dueDate;

    @Column(name = "task_order", nullable = false)
    private Integer order;

    @Column(name = "checklist")
    @Convert(converter = JsonListConverter.class)
    private List<CheckListItem> checklist;

    @Column(name = "is_recurring")
    private boolean isRecurring = false;

    @Enumerated(EnumType.STRING)
    @Column(name = "recurring_type")
    private RecurringType recurringType = RecurringType.WEEKLY;

    @Column(name = "recurring_end_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date recurringEndDate;

    @Column(name = "recurring_on_days")
    @Convert(converter = JsonListConverter.class)
    private List<Integer> recurringOnDays;

    @Column(name = "reminder_settings")
    private String reminderSettings;


    @Column(name = "deleted_at", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date deletedAt;

    @Column(name = "is_deleted")
    private boolean isDeleted = false;

    // 所属用户Id
    @Column(name = "assigned_to")
    private Integer assignedTo;

    @Column(name = "is_team_task", nullable = false)
    private boolean isTeamTask = false;

    @Column(name = "weight", nullable = false)
    private Integer weight = 1;

    @Column(name = "parent_task_id")
    private Integer parentTaskId;

    public enum Priority {
        LOW, MEDIUM, HIGH
    }

    public enum Status {
        TODO, IN_PROGRESS, DONE
    }

    public enum RecurringType {
        WEEKLY, MONTHLY
    }

    public Integer getId() {
        return task_id;
    }

    public void setId(Integer id) {
        this.task_id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Priority getPriority() {
        return priority;
    }

    public void setPriority(Priority priority) {
        this.priority = priority;
    }

    public List<String> getTags() {
        return tags;
    }

    public void setTags(List<String> tags) {
        this.tags = tags;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public Date getDueDate() {
        return dueDate;
    }

    public void setDueDate(Date dueDate) {
        this.dueDate = dueDate;
    }

    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }


    public List<CheckListItem> getChecklist() {
        return checklist;
    }

    public void setChecklist(List<CheckListItem> checklist) {
        this.checklist = checklist;
    }

    public boolean isRecurring() {
        return isRecurring;
    }

    public void setRecurring(boolean recurring) {
        isRecurring = recurring;
    }

    public RecurringType getRecurringType() {
        return recurringType;
    }

    public void setRecurringType(RecurringType recurringType) {
        this.recurringType = recurringType;
    }

    public Date getRecurringEndDate() {
        return recurringEndDate;
    }

    public void setRecurringEndDate(Date recurringEndDate) {
        this.recurringEndDate = recurringEndDate;
    }

    public List<Integer> getRecurringOnDays() {
        return recurringOnDays;
    }

    public void setRecurringOnDays(List<Integer> recurrenceOnDays) {
        this.recurringOnDays = recurrenceOnDays;
    }

    public String getReminderSettings() {
        return reminderSettings;
    }

    public void setReminderSettings(String reminderSettings) {
        this.reminderSettings = reminderSettings;
    }

    public Date getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(Date deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted() {
        return isDeleted;
    }

    public void setDeleted(boolean deleted) {
        isDeleted = deleted;
    }

    public Integer getAssignedTo() {
        return assignedTo;
    }

    public void setAssignedTo(Integer assignedTo) {
        this.assignedTo = assignedTo;
    }

    public boolean isTeamTask() {
        return isTeamTask;
    }

    public void setTeamTask(boolean teamTask) {
        isTeamTask = teamTask;
    }

    public Integer getParentTaskId() { return this.parentTaskId; }

    public void setParentTaskId(Integer parentTaskId) {
        this.parentTaskId = parentTaskId;
    }

    public Integer getWeight() {
        return weight;
    }

    public void setWeight(Integer weight) {
        this.weight = weight;
    }
}
