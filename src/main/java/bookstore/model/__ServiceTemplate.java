package bookstore.model;

import java.util.ArrayList;

import java.util.List;

import java.util.stream.Collectors;

/**
 * ToDo: Template für eine Service-Klasse (Business Logic Layer).
 * <p>
 * Verantwortlichkeiten:
 * <p>
 * - Fachlogik und Geschäftsregeln
 * <p>
 * - Daten verwalten (in-memory, Datenbank, etc.)
 * <p>
 * - Validierung
 * <p>
 * - Exception Handling
 * <p>
 * <p>
 * <p>
 * Wichtig:
 * <p>
 * - Service-Klassen sind oft statisch (nur static Methoden)
 * <p>
 * - Keine direkte Abhängigkeit zu Servlets (lose Kopplung)
 * <p>
 * - Exceptions werfen bei Fehlern (z.B. NotFoundException)
 */

public class __ServiceTemplate {

    /**
     * ToDo: Statische Liste als einfacher In-Memory Datenspeicher.
     * <p>
     * In der Prüfung:
     * <p>
     * - Reicht oft eine einfache ArrayList
     * <p>
     * - Keine echte Datenbank nötig
     * <p>
     * - Daten gehen beim Server-Neustart verloren
     * <p>
     * Alternative: ConcurrentHashMap für Thread-Safety
     */

    private static final List<__EntityTemplate> items = new ArrayList<>();

    /**
     * ToDo: Statischer Initializer-Block für Test-Daten.
     *
     * Wird einmalig beim Laden der Klasse ausgeführt.
     */

    static {
        items.add(new __EntityTemplate(1, "Item 1", "First test item", 10.50, null, true));
        items.add(new __EntityTemplate(2, "Item 2", "Second test item", 20.00, null, true));
        items.add(new __EntityTemplate(3, "Item 3", "Third test item", 15.75, null, false));
    }

    /**
     * ToDo: Alle Items abrufen (READ - Collection).
     * <p>
     * Rückgabe:
     * <p>
     * - Kopie der Liste (Collections.unmodifiableList) für Sicherheit
     * <p>
     * - Oder direkte Liste wenn Performance wichtiger
     */

    public static List<__EntityTemplate> getAll() {
        return items;
    }

    /**
     * ToDo: Gefiltertes Abrufen mit Query-Parameter (SEARCH).
     * <p>
     * Verwendung:
     * <p>
     * - GET /api/items?query=test
     * <p>
     * - Suche in mehreren Feldern
     * <p>
     * - Case-insensitive Suche mit toLowerCase()
     */

    public static List<__EntityTemplate> search(String query) {
        if (query == null || query.isBlank()) {
            return getAll();
        }

        String searchTerm = query.toLowerCase();
        return items.stream()
                .filter(item -> item.getName()
                        .toLowerCase()
                        .contains(searchTerm) || (item.getDescription() != null && item.getDescription()
                        .toLowerCase()
                        .contains(searchTerm)))
                .collect(Collectors.toList());

    }

    /**
     * ToDo: Einzelnes Item per ID abrufen (READ - Single Item).
     * <p>
     * Exception werfen:
     * <p>
     * - Wenn Item nicht gefunden wird
     * <p>
     * - Servlet kann darauf mit 404 reagieren
     * <p>
     * Alternative: Optional<T> zurückgeben
     */

    public static __EntityTemplate getById(Integer id) throws __NotFoundException {

        return items.stream().filter(item -> item.getId().equals(id)).findFirst().orElseThrow(__NotFoundException::new);
    }

    /**
     * ToDo: Neues Item erstellen (CREATE).
     * <p>
     * Schritte:
     * <p>
     * 1. Neue ID generieren
     * <p>
     * 2. ID am Objekt setzen
     * <p>
     * 3. Objekt zur Liste hinzufügen
     * <p>
     * 4. Objekt zurückgeben
     * <p>
     * Validierung vorher durchführen!
     */

    public static __EntityTemplate create(__EntityTemplate item) throws __ValidationException {

        /* ToDo: Validierung durchführen */
        validateItem(item);

        /* ToDo: Neue ID generieren (höchste ID + 1) */
        int newId = items.stream().mapToInt(i -> i.getId()).max().orElse(0) + 1;
        item.setId(newId);

        /* ToDo: Item zur Liste hinzufügen */
        items.add(item);
        return item;

    }

    /**
     * ToDo: Bestehendes Item aktualisieren (UPDATE).
     * <p>
     * Wichtig:
     * <p>
     * - Prüfen ob Item existiert
     * <p>
     * - Alle Felder überschreiben (PUT) oder nur geänderte (PATCH)
     * <p>
     * - ID darf nicht geändert werden
     */

    public static __EntityTemplate update(Integer id, __EntityTemplate updatedItem)
            throws __NotFoundException, __ValidationException {

        /* ToDo: Prüfen ob Item existiert */
        __EntityTemplate existingItem = getById(id);

        /* ToDo: Validierung */
        validateItem(updatedItem);

        /* ToDo: Felder überschreiben, aber ID beibehalten */
        existingItem.setName(updatedItem.getName());

        existingItem.setDescription(updatedItem.getDescription());

        existingItem.setPrice(updatedItem.getPrice());

        existingItem.setActive(updatedItem.isActive());

        return existingItem;

    }

    /**
     * ToDo: Item löschen (DELETE).
     * <p>
     * Optionen:
     * <p>
     * - Physisches Löschen (aus Liste entfernen)
     * <p>
     * - Logisches Löschen (active = false setzen)
     */

    public static void delete(Integer id) throws __NotFoundException {
        __EntityTemplate item = getById(id);

        /* ToDo: Item aus Liste entfernen */
        if (!items.remove(item)) {
            throw new __NotFoundException();
        }

    }

    /**
     * ToDo: Logisches Löschen (Soft Delete).
     * <p>
     * Vorteil: Daten bleiben erhalten, sind aber inaktiv
     */
    public static void softDelete(Integer id) throws __NotFoundException {
        __EntityTemplate item = getById(id);
        item.setActive(false);

    }

    /**
     * ToDo: Validierungsmethode für Business Rules.
     * <p>
     * Beispiele:
     * <p>
     * - Pflichtfelder prüfen
     * <p>
     * - Wertebereich prüfen
     * <p>
     * - Einzigartigkeit prüfen
     * <p>
     * - Format prüfen (Email, URL, etc.)
     */

    private static void validateItem(__EntityTemplate item) throws __ValidationException {

        /* ToDo: Name ist Pflichtfeld */
        if (item.getName() == null || item.getName().isBlank()) {
            throw new __ValidationException("Name is required");

        }

        /* ToDo: Name-Länge prüfen */
        if (item.getName().length() < 3 || item.getName().length() > 100) {
            throw new __ValidationException("Name must be between 3 and 100 characters");

        }

        /* ToDo: Preis muss positiv sein */
        if (item.getPrice() < 0) {
            throw new __ValidationException("Price must be positive");

        }

        /* ToDo: Einzigartigkeit prüfen (Name darf nicht doppelt vorkommen) */
        boolean nameExists = items.stream()
                .anyMatch(i -> !i.getId().equals(item.getId()) && i.getName().equalsIgnoreCase(item.getName()));

        if (nameExists) {
            throw new __ValidationException("Item with this name already exists");
        }

    }

    /**
     * ToDo: Hilfsmethode zum Zurücksetzen der Daten (z.B. für Tests).
     */

    public static void clear() {

        items.clear();

    }

    /**
     * ToDo: Hilfsmethode um zu prüfen ob ein Item existiert.
     * <p>
     * Kann verwendet werden um 409 Conflict zu vermeiden.
     */

    public static boolean exists(Integer id) {

        return items.stream().anyMatch(item -> item.getId().equals(id));

    }

    /**
     * ToDo: Anzahl der Items zurückgeben.
     * <p>
     * Nützlich für Statistiken oder Paginierung.
     */

    public static int count() {
        return items.size();

    }

    /**
     * ToDo: Nur aktive Items zurückgeben.
     */

    public static List<__EntityTemplate> getActive() {

        return items.stream().filter(__EntityTemplate::isActive).collect(Collectors.toList());
    }

}