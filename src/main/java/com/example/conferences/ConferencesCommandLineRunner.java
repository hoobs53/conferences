package com.example.conferences;

import com.example.conferences.model.Lecture;
import com.example.conferences.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;

@Component
public class ConferencesCommandLineRunner implements CommandLineRunner {
    @Autowired
    LectureRepository lectureRepository;

    @Override
    public void run(String [] args) {
        createPath("technology");
        createPath("research");
        createPath("marketing");
    }

    private void createPath(String theme) {

        long milliseconds = 1622541600000L; //UTC Tue Jun 01 2021 10:00:00
        Lecture lecture = new Lecture(null, new ArrayList<>(), new Date(milliseconds) ,theme);
        lectureRepository.save(lecture);

        milliseconds = 1622548800000L; //UTC Tue Jun 01 2021 12:00:00
        lecture = new Lecture(null, new ArrayList<>(), new Date(milliseconds) ,theme);
        lectureRepository.save(lecture);

        milliseconds = 1622556000000L;//UTC Tue Jun 01 2021 14:00:00
        lecture = new Lecture(null, new ArrayList<>(), new Date(milliseconds) ,theme);
        lectureRepository.save(lecture);
    }
}