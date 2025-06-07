package com.kf4b.bitlist.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.io.Serializable;
import java.util.Date;

// 个人用户专注事项
@Entity
@Table(name = "mindeditem")
public class MindedItem {
    @Id
    @GeneratedValue(generator = "uuid")
    private int mindeditem_id;

    @Column(name = "timestamp", nullable = false)
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    @Column(name = "duration_in_seconds", nullable = false)
    private Integer durationInSeconds;

    @Column(name = "user_id", nullable = false)
    private String user_id;

    public Integer getId() { return this.mindeditem_id; }

    public void setId(Integer id) { this.mindeditem_id = id; }

    public Date getTimestamp() { return  this.timestamp; }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public Integer getDurationInSeconds() { return this.durationInSeconds; }

    public void setDurationInSeconds(Integer durationInSeconds) { this.durationInSeconds = durationInSeconds; }

    public String getUserId() { return this.user_id; }

    public void setUserId(String userId) { this.user_id = userId; }
}
