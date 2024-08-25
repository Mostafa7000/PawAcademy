package pawacademy.services;

import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.util.Objects;

public class UriService {
    public static String getUri(String internalPath) {
        return !Objects.isNull(internalPath) ? ServletUriComponentsBuilder.fromCurrentContextPath()
                .path(internalPath)
                .toUriString() : null;
    }
}
