package com.example.conferences.model;

import javax.persistence.*;
import java.sql.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "lectures")
public class Lecture {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @ManyToMany
    @JoinTable(name = "participants", joinColumns = @JoinColumn(name = "lecture_id", referencedColumnName = "id"),
    inverseJoinColumns = @JoinColumn(name = "user_id", referencedColumnName = "id"))
    private List<User> participants = new ArrayList<>();

    private Date date;

    public Time getTime() {
        return time;
    }

    public void setTime(Time time) {
        this.time = time;
    }

    private Time time;
    private String theme;

    public Lecture(Long id, List<User> participants, Date date, Time time, String theme) {
        this.id = id;
        this.participants = participants;
        this.date = date;
        this.theme = theme;
        this.time = time;
    }

    public Lecture() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<User> getParticipants() {
        return participants;
    }

    public void setParticipants(List<User> participants) {
        this.participants = participants;
    }

    public void addParticipant(User user) {
        participants.add(user);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public boolean containsUser(Long uid) {
        return participants.stream()
                .anyMatch(user -> user.getId().equals(uid));
    }

    public void removeUser(Long uid) {
        participants.removeIf(user -> user.getId().equals(uid));
    }
}
