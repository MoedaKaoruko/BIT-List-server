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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date timestamp;

    private Integer durationInSeconds;

    private Integer userId;

    public Integer getId() { return this.id; }

    public void setId(Integer id) { this.id = id; }

    public Date getTimestamp() { return  this.timestamp; }

    public void setTimestamp(Date timestamp) { this.timestamp = timestamp; }

    public Integer getDurationInSeconds() { return this.durationInSeconds; }

    public void setDurationInSeconds(Integer durationInSeconds) { this.durationInSeconds = durationInSeconds; }

    public Integer getUserId() { return this.userId; }

    public void setUserId(Integer userId) { this.userId = userId; }
}
