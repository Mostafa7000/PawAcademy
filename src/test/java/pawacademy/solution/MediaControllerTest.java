package pawacademy.solution;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import pawacademy.services.FileStorageService;
import pawacademy.solution.services.MediaAuthorizationService;
import pawacademy.solution.user.domain.User;
import pawacademy.solution.user.domain.UserRepository;

import java.net.MalformedURLException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MediaControllerTest {

    @InjectMocks
    private MediaController mediaController;

    @Mock
    private FileStorageService fileStorageService;

    @Mock
    private MediaAuthorizationService mediaAuthorizationService;

    @Mock
    private UserRepository userRepository;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("user@example.com");

        org.springframework.security.core.userdetails.User principal = new org.springframework.security.core.userdetails.User(user.getEmail(), "", java.util.Collections.emptyList());
        SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(principal, ""));
    }

    @Test
    public void testGetMedia_Authorized_Succeeds() throws MalformedURLException {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(mediaAuthorizationService.isAuthorized(user, "forum/test.jpg")).thenReturn(true);
        Resource resource = new ByteArrayResource("test content".getBytes());
        when(fileStorageService.loadFileAsResource(anyString())).thenReturn(resource);

        ResponseEntity<Resource> response = mediaController.uploadAvatar("test.jpg", "forum");

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    public void testGetMedia_Unauthorized_Fails() throws MalformedURLException {
        when(userRepository.findByEmail("user@example.com")).thenReturn(Optional.of(user));
        when(mediaAuthorizationService.isAuthorized(user, "forum/test.jpg")).thenReturn(false);

        ResponseEntity<Resource> response = mediaController.uploadAvatar("test.jpg", "forum");

        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
