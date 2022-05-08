package com.example.conferences.model;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;
import java.util.ArrayList;
import java.util.List;

public class Path {
    private String theme;

    private int total_participants;

    private List<Lecture> lectures;

    public Path(String theme, int participants, List<Lecture> lectures) {
        this.theme = theme;
        this.total_participants = participants;
        this.lectures = lectures;
    }

    public Path(String theme) {
        this.theme = theme;
        this.total_participants = 0;
        this.lectures = new ArrayList<>();
    }

    public String getTheme() {
        return theme;
    }

    public void setTheme(String theme) {
        this.theme = theme;
    }

    public int getParticipants() {
        return total_participants;
    }

    public void setParticipants(int participants) {
        this.total_participants = participants;
    }

    public List<Lecture> getLectures() {
        return lectures;
    }

    public void setLectures(List<Lecture> lectures) {
        this.lectures = lectures;
    }
}
