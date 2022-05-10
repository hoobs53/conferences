package com.example.conferences.controller;

import com.example.conferences.model.Conference;
import com.example.conferences.model.Lecture;
import com.example.conferences.model.Path;
import com.example.conferences.model.User;
import com.example.conferences.service.LectureService;
import com.example.conferences.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
public class ConferenceController {

    @Autowired
    Conference conference;
    @Autowired
    UserService userService;
    @Autowired
    LectureService lectureService;

    @GetMapping("/")
    public ResponseEntity<Object> getConferencePlan() {
        Map<String, Object> map = new HashMap<>();
        map.put("paths", conference.getPaths());
        map.put("total_participants", conference.getParticipants());
        return new ResponseEntity<>(map, HttpStatus.OK);
    }

    @PostMapping("/users")
    public ResponseEntity<User> createUser(@RequestBody User user) {
        return new ResponseEntity<>(userService.createNewUser(user), HttpStatus.OK);
    }

    @GetMapping("/users")
    public ResponseEntity<List<User>> getAllUsers() {
        List<User> userList = userService.getAllUsers();
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @GetMapping("/{pathname}/lectures")
    public ResponseEntity<List<Lecture>> getAllLecturesByPath(@PathVariable("pathname") String path) {
        Optional<List<Lecture>> optionalLectures = lectureService.getAllLecturesByPath(path.toLowerCase());
        return optionalLectures.map(
                        lectures -> new ResponseEntity<>(lectures, HttpStatus.OK))
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    @GetMapping("/users/lectures")
    public ResponseEntity<List<Lecture>> getUsersLectures(@RequestParam("email") String email) {
        return userService.getUserByEmail(email).map(
                user -> lectureService.getUsersLectures(user))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.OK)
        );
    }
    @PostMapping("/lectures/{lid}/participants")
    public ResponseEntity<Lecture> registerUserToLecture(@PathVariable("lid") Long lid,
                                                         @RequestBody User user) {
        Optional<Lecture> lectureOptional = lectureService.getLectureById(lid);
        if (lectureOptional.isPresent()) {
            Lecture lecture = lectureOptional.get();
            if (lectureService.ifNotFull(lecture)) {
                System.out.println(lecture.getParticipants().size());
                if (!userService.ifUserExists(user)) {
                    User _user = userService.createNewUser(user);
                    System.out.println("new user");
                    lectureService.registerUser(user, lecture);
                    System.out.println("added user");

                    return new ResponseEntity<>(lecture, HttpStatus.OK);
                }
                return new ResponseEntity<>(HttpStatus.FORBIDDEN);
            } else {
                return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
            }
        } else {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @DeleteMapping("/lectures/{lid}/participants/{uid}")
    public ResponseEntity<Lecture> unregisterUser(@PathVariable("lid") Long lid,
                                                  @PathVariable("uid") Long uid) {
            return lectureService.removeUserFromLecture(lid, uid);
    }

    @PatchMapping("/users/{uid}")
    public ResponseEntity<User> updateUserEmail(@PathVariable("uid") Long uid,
                                                @RequestBody Map<String, String> email) {
        return userService.updateUserEmail(uid, email.get("email"));
    }
}