/**
 ToDo: Router für Single Page Application
 *
 Einsatzzweck:
 - Verwaltet verschiedene Views/Components der App
 - Mappt URL-Pfade auf Components
 - Ermöglicht Navigation ohne Page Reload
 - Verwendet Hash-basiertes Routing (#/path)
 *
 Wichtige Konzepte:
 - Hash-basiertes Routing (location.hash)
 - hashchange Event
 - Component Registration
 - Programmatische Navigation
 - Error Handling (404)
 */

/* ToDo: Object.create(null) erstellt Objekt ohne Prototype
Vermeidet Probleme mit geerbten Properties wie 'constructor' */
const routes = Object.create(null);

export default {
    /**
     ToDo: Route registrieren
     *
     Mappt einen Pfad auf eine Component
     *
     Beispiel: router.register('/home', homeComponent);
     *
     @param {string} path - URL-Pfad (z.B. '/home', '/books')
     @param {Object} component - Component-Objekt mit render() Methode
     */
    register: function (path, component) {
        routes[path] = component;
    }, /**
     ToDo: Programmatisch navigieren
     *
     Beispiel: router.navigate('/books', '978-3-12345-678-9');
     *
     @param {string} path - Ziel-Pfad
     @param {string} param - Optionaler URL-Parameter (z.B. ID)
     */
    navigate: function (path, param) {
        /* ToDo: Parameter an Pfad anhängen falls vorhanden */
        path += param ? '/' + param : '';
        /* ToDo: Hash ändern triggert hashchange Event
        Nur ändern wenn nicht bereits gesetzt (vermeidet doppelte Events) */
        if (location.hash !== '#' + path) {
            location.hash = path;
        } else {
            /* ToDo: Falls Hash bereits korrekt, manuell View updaten */
            updateView();
        }
    }
};

/**
 ToDo: Event Listener für Hash-Änderungen
 *
 Wird getriggert bei:
 - Klick auf <a href="#/path">Link</a>
 - Browser Back/Forward Button
 - router.navigate() Aufruf
 */
window.addEventListener('hashchange', updateView);

/**
 ToDo: View basierend auf aktuellem Hash aktualisieren
 *
 Ablauf:
 1. Hash parsen (Pfad + optionaler Parameter)
 2. Entsprechende Component finden
 3. Component rendern
 4. View in DOM einfügen
 5. Titel setzen
 */
async function updateView() {
    try {
        /* ToDo: Hash parsen
        Beispiel: '#/books/978-3-12345' -> ['', 'books', '978-3-12345']
        decodeURI() dekodiert URL-kodierte Zeichen
        */
        const parts = decodeURI(location.hash).split('/');
        /* ToDo: splice(1) entfernt erstes Element (leerer String vor #) */
        const [path, param] = parts.splice(1);
        /* ToDo: Component für Pfad finden
        '/' + (path || '') behandelt Root-Pfad (#/ oder #)
        */
        const component = routes['/' + (path || '')];
        /* ToDo: 404 wenn Component nicht gefunden */
        if (!component) {
            throw '<h2>404 Not Found</h2><p>Sorry, page not found!</p>';
        }
        /* ToDo: Component rendern
        await falls render() async ist (z.B. wegen AJAX)
        Parameter wird an render() übergeben
        */

        const view = await component.render(param);
        /* ToDo: Main-Element im DOM finden und Inhalt ersetzen
                replaceChildren() entfernt alte Kinder und fügt neue ein
                */
        document.querySelector('main').replaceChildren(view);
        /* ToDo: Browser-Titel setzen
        Fallback auf 'Bookstore' falls component.title nicht definiert
        */
        document.title = 'Bookstore' + (component.title ? ' - ' + component.title : '');
    } catch (error) {
        /* ToDo: Error Handling
        error kann sein:
        - HTML-String (404-Nachricht)
        - Error-Objekt (JS-Fehler in Component)
        */
        console.error(error);
        /* ToDo: Error-View erstellen und anzeigen */
        const view = document.createElement('div');
        view.innerHTML = error;
        document.querySelector('main').replaceChildren(view);
    }
}