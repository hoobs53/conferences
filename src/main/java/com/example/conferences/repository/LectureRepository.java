package com.example.conferences.repository;

import com.example.conferences.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Query("SELECT l FROM Lecture l WHERE l.theme = ?1")
    Optional<List<Lecture>> findByTheme(String theme);
    @Query("SELECT l FROM Lecture l WHERE l.theme = ?1 AND l.id = ?2")
    Optional<Lecture> findByThemAndId(String theme, Long id);

    @Query(value = "SELECT LECTURE_ID, COUNT(*) AS C " +
            "FROM (SELECT * FROM PARTICIPANTS) " +
            "GROUP BY LECTURE_ID ", nativeQuery = true)
    List<List<Object>> findMostPopularLecturesById();
    @Query(value = "SELECT THEME, COUNT(*) AS C FROM PARTICIPANTS " +
            "LEFT JOIN LECTURES " +
            "ON PARTICIPANTS.LECTURE_ID=LECTURES.ID " +
            "GROUP BY THEME ", nativeQuery = true)
    List<List<Object>> findMostPopularLecturesByTheme();
}