/**
 ToDo: Main Entry Point für die Single-Page Application
 *
 Diese Datei ist der zentrale Einstiegspunkt und orchestriert:
 - Import aller benötigten Module (Router, Components, Service, Store)
 - Registrierung aller Routes mit ihren Components
 - Initiale Navigation zur Startseite
 - Globale Event Handler (z.B. für Navigation, Logout)
 *
 Wichtig für die Prüfung:
 - Diese Datei wird als type="module" im HTML eingebunden
 - Alle Imports müssen mit .js Endung sein
 - Die Navigation sollte erst nach dem Registrieren der Routes starten
 */

/* ToDo: Router importieren - verwaltet die Navigation zwischen Components */
import router from './router.js';

/* ToDo: Service importieren - zentrale API-Schnittstelle für AJAX Requests */
import service from './service.js';

/* ToDo: Store importieren - zentrale Datenverwaltung (Application State) */
import store from './store.js';

/* ToDo: Alle Components importieren */
import home from './components/home.js';

import bookDetail from './components/book-detail.js';
import order from './components/order.js';
import login from './components/login.js';
import notFound from './components/not-found.js';

/**
 ToDo: Routes registrieren
 *
 Syntax: router.register(path, component, guard)
 - path: URL-Pfad (z.B. '/books', '/order')
 - component: Das Component-Objekt mit render() Methode
 - guard: Optional - Funktion die true/false zurückgibt (z.B. Login-Check)
 *
 Beispiele:
 - '/' -> Home-Seite
 - '/books/:isbn' -> Detail-Seite mit Parameter
 - '/order' -> Nur für eingeloggte User (mit Guard)
 */

// ToDo: Öffentliche Routes (ohne Login)
router.register('/', home);
router.register('/home', home);
router.register('/books', home);
router.register('/book', bookDetail); // Mit Parameter: /book/978-3-86490-207-9
router.register('/login', login);

// ToDo: Geschützte Routes (nur für eingeloggte User)
// Guard prüft ob User im Store vorhanden ist
router.register('/order', order, () => !!store.getUser());

// ToDo: 404 Fallback für nicht existierende Routes
// router.register('*', notFound);
/**

 ToDo: Globale Event Listener registrieren
 *
 Diese Event Listener sind unabhängig von Components und
 bleiben während der gesamten Laufzeit der App aktiv.
 *
 Typische Anwendungsfälle:
 - Navigation über Menü-Links
 - Logout-Button
 - Globale Keyboard-Shortcuts
 - Window Events (resize, beforeunload)
 */

// ToDo: Navigation über Menü (Event Delegation auf document)
document.addEventListener('click', (event) => {

    /* ToDo: Prüfen ob ein Navigation-Link geklickt wurde
    data-navigate Attribut enthält den Zielpfad
    */
    const navLink = event.target.closest('[data-navigate]');

    if (navLink) {
        event.preventDefault(); // Verhindert Standard-Link-Verhalten
        const path = navLink.dataset.navigate;
        router.navigate(path);
    }
});

// ToDo: Logout-Handler (Event Delegation)
document.addEventListener('click', (event) => {
    if (event.target.matches('[data-action="logout"]')) {
        event.preventDefault();

        /* ToDo: User aus Store entfernen */
        store.clear();

        /* ToDo: Navigation zur Login-Seite */
        router.navigate('/login');
        console.log('User logged out');
    }
});

/**
 ToDo: Initiale Daten laden (Optional)
 *
 Wenn bestimmte Daten bereits beim App-Start geladen werden sollen,
 kann das hier geschehen (z.B. User-Session wiederherstellen).
 */

// ToDo: Beispiel - User-Session aus localStorage wiederherstellen
function restoreSession() {
    const userData = localStorage.getItem('user');
    if (userData) {
        try {
            const user = JSON.parse(userData);
            store.setUser(user);
            console.log('Session restored for user:', user.name);
        } catch (error) {
            console.error('Failed to restore session:', error);
            localStorage.removeItem('user');
        }
    }
}

// restoreSession(); // Aktivieren falls gewünscht

/**
 ToDo: Initiale Navigation starten
 *
 WICHTIG: Die Navigation muss NACH dem Registrieren aller Routes erfolgen!
 *
 Optionen:
 1. Zur Root navigieren: router.navigate('/')
 2. Aktuelle Hash-URL verwenden: router.navigate(location.hash || '/')
 3. Default-Route: router.navigate('/home')
 */

// ToDo: Navigation zur aktuellen URL oder Fallback zur Home-Seite
const initialPath = location.hash ? location.hash.substring(1) : '/';
router.navigate(initialPath);

/**
 ToDo: Window Events (Optional)
 *
 Reagiere auf globale Browser-Events
 */

// ToDo: Warnung beim Verlassen der Seite (z.B. wenn Formular nicht gespeichert)
window.addEventListener('beforeunload', (event) => {

    /* ToDo: Nur warnen wenn ungespeicherte Änderungen vorhanden */
    if (store.hasUnsavedChanges && store.hasUnsavedChanges()) {
        event.preventDefault();
        event.returnValue = ''; // Chrome benötigt dies
        return 'You have unsaved changes. Are you sure you want to leave?';
    }
});

// ToDo: Responsive Handling bei Window Resize
let resizeTimer;
window.addEventListener('resize', () => {

    /* ToDo: Debounce - verhindert zu häufige Aufrufe */
    clearTimeout(resizeTimer);
    resizeTimer = setTimeout(() => {
        console.log('Window resized:', window.innerWidth, 'x', window.innerHeight);

        /* ToDo: Hier könnte man responsive Anpassungen vornehmen */
    }, 250);
});

/**
 ToDo: Error Handling (Optional aber empfohlen)
 *
 Globale Error Handler für unkontrollierte Fehler
 */

// ToDo: Uncaught Errors abfangen
window.addEventListener('error', (event) => {
    console.error('Global error caught:', event.error);

// ToDo: Fehler an Monitoring-Service senden oder User informieren
});

// ToDo: Unhandled Promise Rejections abfangen
window.addEventListener('unhandledrejection', (event) => {
    console.error('Unhandled promise rejection:', event.reason);

// ToDo: Fehler behandeln
});

/**
 ToDo: Development Helper (nur für Entwicklung)
 *
 Diese Hilfsfunktionen erleichtern das Debugging
 */

// ToDo: Globalen Zugriff auf wichtige Objekte für Debugging (NUR in Development!)
if (window.location.hostname === 'localhost') {
    window.debug = {
        router,
        store,
        service,
        navigateTo: (path) => router.navigate(path),
        clearStore: () => store.clear(),
        getState: () => ({
            user: store.getUser(), books: store.getBooks()
        })
    };
    console.log('Debug mode enabled. Access via window.debug');
}

/* ToDo: Export (falls andere Module darauf zugreifen müssen) */
export default {
    router, store, service
};