package pawacademy.solution.user;

import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final Path absoluteDirectory = Paths.get("").toAbsolutePath().normalize();
    private final Path mediaDirectory = absoluteDirectory.resolve("media");


    public FileStorageService() {
        try {
            Files.createDirectories(mediaDirectory);
        } catch (IOException e) {
            throw new RuntimeException("Could not create the directory where the uploaded files will be stored.", e);
        }
    }

    public String storeFile(MultipartFile file, String entity) {
        String fileName = UUID.randomUUID() + "_" + file.getOriginalFilename();
        try {
            Path entityDirectory = mediaDirectory.resolve(entity);
            Files.createDirectories(entityDirectory);

            Path targetLocation = entityDirectory.resolve(fileName);
            Files.write(targetLocation, file.getBytes());

            return "/" + absoluteDirectory.relativize(targetLocation).toString().replace("\\", "/");
        } catch (IOException e) {
            throw new RuntimeException("Could not store file " + fileName + ". Please try again!", e);
        }
    }

    /**
     * Delete Files
     *
     * @param fileName the name including every directory from the media directory
     */
    public void deleteFile(String fileName) throws IOException {
        Path toBeDeleted = absoluteDirectory.resolve(fileName).normalize();
        Files.delete(toBeDeleted);
    }

    public Resource loadFileAsResource(String fileName) throws MalformedURLException {
        return new UrlResource(mediaDirectory.resolve(fileName).normalize().toUri());
    }
}
