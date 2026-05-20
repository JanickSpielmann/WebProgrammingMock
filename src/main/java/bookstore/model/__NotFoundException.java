package bookstore.model;

/**
 * ToDo: Custom Exception für "nicht gefunden" Fehler.
 * <p>
 * <p>
 * <p>
 * Verwendung:
 * <p>
 * - Wird vom Service geworfen wenn ein Item nicht existiert
 * <p>
 * - Servlet fängt diese Exception und sendet 404 Not Found
 * <p>
 * <p>
 * <p>
 * Extends Exception = Checked Exception (muss deklariert werden)
 * <p>
 * Extends RuntimeException = Unchecked Exception (muss nicht deklariert werden)
 */

public class __NotFoundException extends Exception {

    /**
     * ToDo: Leerer Konstruktor für einfache Verwendung.
     */

    public __NotFoundException() {
        super();
        // no arguments constructor
    }

    /**
     * ToDo: Konstruktor mit Fehlermeldung.
     * <p>

     * Beispiel: throw new __NotFoundException("Item with id 5 not found");
     */

    public __NotFoundException(String message) {
        super(message);
    }

    /**
     * ToDo: Konstruktor mit Fehlermeldung und Ursache.
     * <p>
     * Nützlich um Exception-Chains zu bilden.
     */

    public __NotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}