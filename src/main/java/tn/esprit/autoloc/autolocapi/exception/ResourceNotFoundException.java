package tn.esprit.autoloc.autolocapi.exception;

/** Levée quand une entité demandée n'existe pas (HTTP 404). */
public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String ressource, Long id) {
        super(ressource + " introuvable avec l'id " + id);
    }

    public ResourceNotFoundException(String message) {
        super(message);
    }
}
