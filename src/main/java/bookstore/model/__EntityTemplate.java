package bookstore.model;


import java.time.LocalDate;
import java.util.Objects;

/**
 * ToDo: Template für eine einfache Entity-Klasse (POJO / Java Bean).
 * <p>
 * <p>
 * <p>
 * Wichtig für die Prüfung:
 * - Private Felder für Datenkapselung
 * <p>
 * - Getter-Methoden für den Zugriff auf Daten
 * <p>
 * - Optional: Setter-Methoden wenn Daten veränderbar sein sollen
 * <p>
 * - Konstruktor mit allen wichtigen Parametern
 * <p>
 * - Optional: Leerer Konstruktor für Frameworks (Jackson, JPA)
 * <p>
 * - toString(), equals(), hashCode() bei Bedarf
 * <p>
 * <p>
 * <p>
 * Diese Klasse wird verwendet für:
 * <p>
 * - Datentransfer zwischen Layers (DTO)
 * <p>
 * - JSON Serialisierung/Deserialisierung
 * <p>
 * - Datenbank-Mapping
 */

public class __EntityTemplate {

/* ToDo: Private Felder für alle Eigenschaften der Entity.

Verwende passende Datentypen:

- String für Text

- int/Integer für Ganzzahlen

- double/Double für Dezimalzahlen

- boolean/Boolean für Ja/Nein-Werte

- LocalDate für Datum

- LocalDateTime für Datum mit Zeit

*/

    private Integer id;

    private String name;

    private String description;

    private double price;

    private LocalDate createdAt;

    private boolean active;

    /**
     * ToDo: Leerer Konstruktor (Default Constructor).
     * <p>
     * <p>
     * <p>
     * Wird benötigt von:
     * <p>
     * - Jackson für JSON Deserialisierung
     * <p>
     * - JPA/Hibernate für Datenbank-Mapping
     * <p>
     * <p>
     * <p>
     * Ohne diesen Konstruktor kann Jackson kein Objekt aus JSON erstellen!
     */

    public __EntityTemplate() {
        // no arguments constructor
    }

    /**
     * ToDo: Konstruktor mit allen Pflichtfeldern.
     * <p>
     * <p>
     * <p>
     * Vorteile:
     * <p>
     * - Objekte werden vollständig initialisiert
     * <p>
     * - Unveränderliche Objekte möglich (final fields)
     * <p>
     * - Klarer API für Objekt-Erstellung
     */

    public __EntityTemplate(
            Integer id, String name, String description, double price,

            LocalDate createdAt, boolean active) {

        this.id = id;

        this.name = name;

        this.description = description;

        this.price = price;

        this.createdAt = createdAt;

        this.active = active;

    }

    /**
     * ToDo: Konstruktor ohne ID für neue Objekte.
     * <p>
     * <p>
     * <p>
     * ID wird oft erst beim Speichern in der Datenbank generiert.
     */

    public __EntityTemplate(String name, String description, double price) {

        this.name = name;

        this.description = description;

        this.price = price;

        this.createdAt = LocalDate.now();

        this.active = true;

    }

    /* ==================== GETTER METHODEN ====================

ToDo: Getter-Methoden ermöglichen Zugriff auf private Felder.

*

Wichtig:

- Methodenname beginnt mit "get" + Feldname (camelCase)

- Für boolean: "is" statt "get" möglich

- Jackson braucht Getter für JSON Serialisierung

*/

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }


    public boolean isActive() {
        return active;
    }

/* ==================== SETTER METHODEN ====================

ToDo: Setter-Methoden ermöglichen Änderung von Feldern.

*

Wann verwenden:

- Wenn Objekte veränderbar sein sollen (mutable)

- Jackson braucht Setter für JSON Deserialisierung

*

Wann NICHT verwenden:

- Bei unveränderlichen Objekten (immutable)

- Bei Feldern die nur einmal gesetzt werden (z.B. ID)

*/

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    public void setActive(boolean active) {
        this.active = active;
    }
}