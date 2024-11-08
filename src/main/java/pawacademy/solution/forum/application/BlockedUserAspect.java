package pawacademy.solution.forum.application;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import pawacademy.AuthorizationException;
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

    // Intercepts all methods in any class annotated with @RestController
    @Around("@within(org.springframework.web.bind.annotation.RestController)")
    public Object checkBlockedUser(ProceedingJoinPoint joinPoint) throws Throwable {
        User currentUser = getCurrentUser();
        if (currentUser != null && blockedUsersRepository.existsByUserId(currentUser.getId())) {
            throw new AuthorizationException("You are blocked. Contact The Admin");
        }

        return joinPoint.proceed();
    }

    private User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            return null;
        }

        String email = authentication.getName();
        return userRepository.findByEmail(email).orElse(null);
    }
}
