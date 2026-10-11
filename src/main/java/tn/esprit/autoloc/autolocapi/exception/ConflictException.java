package tn.esprit.autoloc.autolocapi.exception;

/** Levée en cas de doublon ou de conflit avec l'état existant (HTTP 409). */
public class ConflictException extends RuntimeException {

    public ConflictException(String message) {
        super(message);
    }
}
