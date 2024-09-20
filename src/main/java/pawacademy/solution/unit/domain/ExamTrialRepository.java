package pawacademy.solution.unit.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExamTrialRepository extends JpaRepository<ExamTrial, Long> {
    List<ExamTrial> findExamTrialsByUserId(Long aLong);
}
