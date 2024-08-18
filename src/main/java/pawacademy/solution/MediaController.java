package pawacademy.solution;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pawacademy.solution.user.FileStorageService;

import java.net.MalformedURLException;

@RestController
@RequestMapping("/media")
public class MediaController {
    @Autowired
    private FileStorageService fileStorageService;

    @GetMapping("/{entity}/{file-name}")
    public ResponseEntity<Resource> uploadAvatar(@PathVariable("file-name") String fileName, @PathVariable String entity) throws MalformedURLException {
        Resource resource = fileStorageService.loadFileAsResource(entity +"/"+ fileName);

        if (resource.exists()) {
            return ResponseEntity.ok()
                    .contentType(MediaType.IMAGE_JPEG)
                    .body(resource);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}
