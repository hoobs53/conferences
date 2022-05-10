package com.example.conferences.service;

import com.example.conferences.model.Lecture;
import com.example.conferences.model.User;
import com.example.conferences.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class LectureService {
    @Autowired
    LectureRepository lectureRepository;

    public void registerUser(User user, Lecture lecture) {
        Lecture response = lectureRepository.getById(lecture.getId());
        response.addParticipant(user);
        lectureRepository.save(response);
    }
    public List<Lecture> getAllLectures() {
        return lectureRepository.findAll();
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

    public ResponseEntity<List<Lecture>> getUsersLectures(User user) {
        List<Lecture> lectureList = lectureRepository.findAll();
        List<Lecture> response = lectureList.stream()
                .filter(lecture -> lecture.getParticipants().contains(user))
                .collect(Collectors.toList());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
    public boolean ifUserRegisteredOnDate(Date date, User user) {
        List<Lecture> lectureList = lectureRepository.findAll();
        return lectureList.stream()
                .anyMatch(lecture ->
                        lecture.getDate().equals(date) && lecture.containsUser(user.getId()));
    }
}
