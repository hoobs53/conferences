package com.example.conferences.controller;

import com.example.conferences.exceptions.LectureFullException;
import com.example.conferences.exceptions.UserNotFreeException;
import com.example.conferences.model.Lecture;
import com.example.conferences.model.User;
import com.example.conferences.service.LectureService;
import com.example.conferences.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.Errors;
import org.springframework.validation.FieldError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@RestController
public class ConferenceController {

    @Autowired
    UserService userService;
    @Autowired
    LectureService lectureService;

    @GetMapping("/stats")
    public ResponseEntity<Object> getMostPopular(@RequestParam("type") String type) {
        switch(type) {
            case("id"): {
                List<Map<String, Object>> statistics = lectureService.getMostPopularLecturesById();
                return new ResponseEntity<>(statistics, HttpStatus.OK);
            }
            case("theme"): {
                List<Map<String, Object>> statistics = lectureService.getMostPopularLecturesByTheme();
                return new ResponseEntity<>(statistics, HttpStatus.OK);
            }
            default: return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }

    @GetMapping("/")
    public ResponseEntity<Object> getConferencePlan() {
        Map<String, Object> response = new HashMap<>();
        Map<String, Object> themes = new HashMap<>();
        List<Lecture> lectureList = lectureService.getAllLectures();

        List<Lecture> themeLectureList = lectureList.stream().filter(
                lecture -> lecture.getTheme().equals("technology")
        ).collect(Collectors.toList());
        themes.put("technology", themeLectureList);

        themeLectureList = lectureList.stream().filter(
                lecture -> lecture.getTheme().equals("marketing")
        ).collect(Collectors.toList());
        themes.put("marketing", themeLectureList);

        themeLectureList = lectureList.stream().filter(
                lecture -> lecture.getTheme().equals("research")
        ).collect(Collectors.toList());
        themes.put("research", themeLectureList);

        response.put("paths", themes);
        return new ResponseEntity<>(response, HttpStatus.OK);
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
    public ResponseEntity<List<Lecture>> getUsersLectures(@RequestParam("login") String login) {
        return userService.getUserByLogin(login).map(
                user -> lectureService.getUsersLectures(user))
                .orElseGet(() -> new ResponseEntity<>(null, HttpStatus.OK)
        );
    }
    @PostMapping("/lectures/{lid}/participants")
    public ResponseEntity<Object> registerUserToLecture(@PathVariable("lid") Long lid,
                                                         @RequestBody @Valid User user, Errors errors) {

        if (errors.hasFieldErrors()) { //when User fields are invalid
            FieldError fieldError = errors.getFieldError();
            assert fieldError != null;
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(fieldError.getDefaultMessage());
        }

        Optional<Lecture> lectureOptional = lectureService.getLectureById(lid);
        if (lectureOptional.isPresent()) {
            Lecture lecture = lectureOptional.get();
            if (lectureService.ifNotFull(lecture)) {
                Optional<User> userOptional = userService.getUserByLoginAndEmail(user.getLogin(), user.getEmail());
                user = userOptional.isPresent()
                        ? userOptional.get() : userService.createNewUser(user);

                if (!lecture.containsUser(user.getId())) {
                    if (lectureService.ifUserRegisteredOnDate(lecture.getDate(), user.getId()))
                        throw new UserNotFreeException(); //user already has a lecture registered on given date

                    lectureService.registerUser(user, lecture.getId());
                    sendEmailConfirmation(user, lecture);
                }
                return new ResponseEntity<>(lecture, HttpStatus.OK);
            } else  //lecture is full
                throw new LectureFullException();
        } else //lecture with a given id doesn't exist
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    private void sendEmailConfirmation(User user, Lecture lecture) {
        try {
            FileWriter notificationFileWriter = new FileWriter("powiadomienia.txt", true);
            String date = "Data: " + new Date(System.currentTimeMillis()) + "\n";
            String to = "Do: " + user.getEmail() + "\n";
            String message = "Tre????: Zosta??e?? zapisany na prelekcj?? o tematyce: " + lecture.getTheme()
                    + ". Spotkanie odb??dzie si??: " + lecture.getDate() + "\n\n";

            notificationFileWriter.append(date);
            notificationFileWriter.append(to);
            notificationFileWriter.append(message);
            notificationFileWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred with opening notification file");
        }
    }

    @DeleteMapping("/lectures/{lid}/participants/{uid}")
    public ResponseEntity<Lecture> unregisterUser(@PathVariable("lid") Long lid,
                                                  @PathVariable("uid") Long uid) {
            return lectureService.removeUserFromLecture(lid, uid);
    }

    @PatchMapping("/users/{uid}")
    public ResponseEntity<User> updateUserEmail(@PathVariable("uid") Long uid,
                                                @RequestBody Map<String, @Email String> email) {
        return userService.updateUserEmail(uid, email.get("email"));
    }
}