package com.example.conferences.service;

import com.example.conferences.model.Lecture;
import com.example.conferences.model.User;
import com.example.conferences.repository.LectureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.math.BigInteger;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class LectureService {
    @Autowired
    LectureRepository lectureRepository;

    public void registerUser(User user, Long lectureId) {
        Lecture response = lectureRepository.getById(lectureId);
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
                lectureRepository.save(lecture);
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
    public boolean ifUserRegisteredOnDate(Date date, Long userId) {
        List<Lecture> lectureList = lectureRepository.findAll();
        return lectureList.stream()
                .anyMatch(lecture ->
                        lecture.getDate().equals(date) && lecture.containsUser(userId));
    }

    public List<Map<String, Object>> getMostPopularLecturesById() {
        List<List<Object>> objects = lectureRepository.findMostPopularLecturesById();
        List<Map<String, Object>> results = new ArrayList<>();
        for (List<Object> list: objects) {
            Map<String, Object> row = new HashMap<>();
            row.put("lecture_id", list.get(0));
            float arg1AsFloat = (float) (((BigInteger)list.get(1)).intValue());
            row.put("users_percentage", arg1AsFloat/getNumberOfParticipants()*100);
            row.put("users_total", list.get(1));
            results.add(row);
        }
        return results;
    }
    public List<Map<String, Object>> getMostPopularLecturesByTheme() {
        List<List<Object>> objects = lectureRepository.findMostPopularLecturesByTheme();
        List<Map<String, Object>> results = new ArrayList<>();
        for (List<Object> list: objects) {
            Map<String, Object> row = new HashMap<>();
            row.put("theme", list.get(0));
            float arg1AsFloat = (float) (((BigInteger)list.get(1)).intValue());
            row.put("users_percentage", arg1AsFloat/getNumberOfParticipants()*100);
            row.put("users_total", list.get(1));
            results.add(row);
        }
        return results;
    }

    private int getNumberOfParticipants() {
        int numberOfParticipants = 0;
        List<Lecture> lectureList = lectureRepository.findAll();
        for (Lecture l: lectureList) {
            numberOfParticipants += l.getParticipants_number();
        }
        return numberOfParticipants;
    }
}
