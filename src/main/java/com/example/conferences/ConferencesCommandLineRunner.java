package com.example.conferences;

import com.example.conferences.model.Conference;
import com.example.conferences.model.Lecture;
import com.example.conferences.model.Path;
import com.example.conferences.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConferencesCommandLineRunner implements CommandLineRunner {
    @Autowired
    Conference conference;
    @Autowired
    LectureRepository lectureRepository;

    @Override
    public void run(String [] args) {
        createPath("Theme1");
        createPath("Theme2");
        createPath("Theme3");
    }

    private void createPath(String theme) {
        Path path = new Path(theme);
        List<Lecture> lectureList = path.getLectures();

        Lecture lecture = new Lecture(null, new ArrayList<>(), new Date(1622534400000L), new Time(1622534400000L),theme);
        lectureRepository.save(lecture);
        path.getLectures().add(lecture);

        lecture = new Lecture(null, new ArrayList<>(), new Date(1622541600000L), new Time(1622541600000L),theme);
        lectureRepository.save(lecture);
        path.getLectures().add(lecture);

        lecture = new Lecture(null, new ArrayList<>(), new Date(1622548800000L), new Time(1622548800000L),theme);
        lectureRepository.save(lecture);
        path.getLectures().add(lecture);

        conference.getPaths().add(path);
    }
}