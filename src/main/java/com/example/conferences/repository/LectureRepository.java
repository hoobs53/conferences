package com.example.conferences.repository;

import com.example.conferences.model.Lecture;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LectureRepository extends JpaRepository<Lecture, Long> {
    @Query("SELECT l FROM Lecture l WHERE l.theme = ?1")
    Optional<List<Lecture>> findByTheme(String theme);
    @Query("SELECT l FROM Lecture l WHERE l.theme = ?1 AND l.id = ?2")
    Optional<Lecture> findByThemAndId(String theme, Long id);
}