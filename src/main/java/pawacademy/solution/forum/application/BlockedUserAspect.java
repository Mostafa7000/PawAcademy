package pawacademy.solution.forum.aspect;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pawacademy.ResponseException;
import pawacademy.solution.forum.domain.BlockedUsersRepository;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

@Aspect
@Component
public class BlockedUserAspect {

    @Autowired
    private BlockedUsersRepository blockedUsersRepository;

    @Autowired
    private UserRepository userRepository;

    @Around("execution(* pawacademy.solution.forum.application.PostController.*(..))")
    public Object checkBlockedUser(ProceedingJoinPoint joinPoint) throws Throwable {
        User currentUser = getCurrentUser();

        if (currentUser != null && blockedUsersRepository.existsByUserId(currentUser.getId())) {
            throw new ResponseException("You are blocked from performing this action.", HttpStatus.FORBIDDEN.value());
        }

        return joinPoint.proceed();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}