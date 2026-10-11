package tn.esprit.autoloc.autolocapi.exception;

/** Levée quand une règle métier n'est pas respectée (HTTP 400). */
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
