package bookstore.model;

/**
 * ToDo: Custom Exception für Validierungsfehler.
 * <p>
 * <p>
 * <p>
 * Verwendung:
 * <p>
 * - Wird vom Service geworfen wenn Daten ungültig sind
 * <p>
 * - Servlet fängt diese Exception und sendet 400 Bad Request
 * <p>
 * <p>
 * <p>
 * Vorteil gegenüber generischer Exception:
 * <p>
 * - Klarere Semantik
 * <p>
 * - Unterscheidung verschiedener Fehlertypen möglich
 * <p>
 * - Spezifisches Exception Handling im Servlet
 */

public class __ValidationException extends Exception {

    /**
     * ToDo: Konstruktor mit Fehlermeldung (meistens ausreichend).
     */

    public __ValidationException(String message) {
        super(message);
        // no arguments constructor
    }

    /**
     * ToDo: Konstruktor mit Fehlermeldung und Ursache.
     */

    public __ValidationException(String message, Throwable cause) {

        super(message, cause);

    }

}