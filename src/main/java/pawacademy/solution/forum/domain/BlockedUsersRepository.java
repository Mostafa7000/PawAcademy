package pawacademy.solution.forum.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BlockedUsersRepository extends JpaRepository<BlockedUser, Long> {
    Optional<BlockedUser> deleteBlockedUserByUserId(Long id);

    Boolean existsByUserId(Long id);
}
