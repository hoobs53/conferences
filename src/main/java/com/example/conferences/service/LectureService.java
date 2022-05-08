package com.example.conferences.service;

import com.example.conferences.exceptions.LectureFullException;
import com.example.conferences.model.Lecture;
import com.example.conferences.model.User;
import com.example.conferences.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LectureService {
    @Autowired
    LectureRepository lectureRepository;

    public Lecture registerUser(User user, Lecture lecture) {
        lecture.addParticipant(user);
        return lecture;
    }
    public ResponseEntity<List<Lecture>> getAllLectures() {
        return new ResponseEntity<>(lectureRepository.findAll(), HttpStatus.OK);
    }

    public boolean ifNotFull(Lecture lecture) {
        return lecture.getParticipants().size() < 5;
    }

    public List<Lecture> getAllLecturesByPath(String theme) {
        Optional<Lecture> lectureOptional = lectureRepository.findByTheme(theme);
        return lectureOptional.stream()
                .collect(Collectors.toList());
    }

    public Optional<Lecture> getLectureByThemeAndId(String path, Long lid) {
        return lectureRepository.findByThemAndId(path, lid);
    }
}
