package com.example.conferences.service;

import com.example.conferences.model.Lecture;
import com.example.conferences.model.User;
import com.example.conferences.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LectureService {
    @Autowired
    LectureRepository lectureRepository;

    public void registerUser(User user, Lecture lecture) {
        Lecture response = lectureRepository.getById(lecture.getId());
        response.addParticipant(user);
        lectureRepository.save(response);
    }
    public ResponseEntity<List<Lecture>> getAllLectures() {
        return new ResponseEntity<>(lectureRepository.findAll(), HttpStatus.OK);
    }

    public boolean ifNotFull(Lecture lecture) {
        return lecture.getParticipants().size() < 5;
    }

    public Optional<List<Lecture>> getAllLecturesByPath(String theme) {
        return lectureRepository.findByTheme(theme);
    }

    public Optional<Lecture> getLectureById(Long lid) {
        return lectureRepository.findById(lid);
    }

    public ResponseEntity<Lecture> removeUserFromLecture(Long lid, Long uid) {
        Optional<Lecture> lectureOptional = lectureRepository.findById(lid);
        if (lectureOptional.isPresent()) {
            Lecture lecture = lectureOptional.get();
            if(lecture.containsUser(uid)) {
                lecture.removeUser(uid);
                return new ResponseEntity<>(lecture, HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
