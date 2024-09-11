package pawacademy.solution.unit.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UnitRepository extends JpaRepository<Unit, Long> {
    @Query(value = "SELECT u.id, u.name, u.description, u.image, u.exam," +
            "COUNT(DISTINCT c.id) AS completed_lessons, " +
            "COUNT(DISTINCT l.id) AS total_lessons " +
            "FROM units u " +
            "LEFT JOIN lessons l ON l.unit_id = u.id " +
            "LEFT JOIN completed_lessons c ON l.id = c.lesson_id AND c.user_id = :userId " +
            "GROUP BY u.id, u.name",
            nativeQuery = true)
    List<Object[]> findWithCompletedLessonsAndTotalLessonsByUserId(@Param("userId") Long userId);

    @Query("select u.image from Unit u where u.id = :unitId")
    String findImageByUnitId(Long unitId);
}
