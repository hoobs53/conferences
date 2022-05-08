package com.example.conferences.model;

import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class Conference {
    public Conference() {
        this.paths = new ArrayList<>();
        this.participants = 0;
    }

    public List<Path> getPaths() {
        return paths;
    }
    private List<Path> paths;

    private int participants;

    public int getParticipants() {
        return paths.stream().mapToInt(Path::getParticipants).sum();
    }
}
