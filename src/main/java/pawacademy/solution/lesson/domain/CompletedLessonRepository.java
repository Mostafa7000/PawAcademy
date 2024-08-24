package pawacademy.solution.lesson.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CompletedLessonRepository extends JpaRepository<CompletedLesson, Long> {
    Optional<CompletedLesson> findByLessonIdAndUserId(Long lessonId, Long userId);
}
