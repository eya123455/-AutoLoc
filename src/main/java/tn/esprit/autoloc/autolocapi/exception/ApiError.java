package tn.esprit.autoloc.autolocapi.exception;

import java.time.LocalDateTime;
import java.util.Map;

/** Corps JSON renvoyé pour toutes les erreurs de l'API. */
public record ApiError(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        Map<String, String> fieldErrors
) {
}
