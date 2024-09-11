package pawacademy.solution.lesson.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface LessonRepository extends JpaRepository<Lesson, Long> {
    List<Lesson> findAllByUnitId(Long unitId);

    @Modifying
    @Query(value = "insert into lessons (unit_id, name, content, video) values (:unitId, :name, :content, :video)", nativeQuery = true)
    void insert(@Param("unitId") Long unitId, @Param("name") String name, @Param("content") String content, @Param("video") String video);

    @Query(value = "SELECT LAST_INSERT_ID()", nativeQuery = true)
    Long getLastInsertedId();

    @Modifying
    @Query(value = "update lessons set name = :name, content = :content, video = :video where id = :id", nativeQuery = true)
    void update(@Param("id") Long id, @Param("name") String name, @Param("content") String content, @Param("video") String video);
}
