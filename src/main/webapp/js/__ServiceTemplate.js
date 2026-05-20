/**
 ToDo: Service-Modul für zentrale API-Kommunikation
 *
 Einsatzzweck:
 - Alle AJAX/Fetch Requests werden hier zentralisiert
 - Vermeidet Code-Duplikation
 - Erleichtert spätere Änderungen der API
 - Verwendet moderne Fetch API mit Promises
 *

 Wichtige Konzepte:
 - BASE_URL für API-Endpunkt
 - Promises für asynchrone Operationen
 - Error Handling mit .catch()
 - JSON Serialisierung/Deserialisierung
 - HTTP Headers (Authorization, Content-Type, Accept)
 */

/* ToDo: Basis-URL für alle API-Requests
Wird allen Pfaden vorangestellt */

const BASE_URL = '/api';

export default {

    /**
     ToDo: GET Request - Liste aller Ressourcen abrufen
     *
     Beispiel: service.getItems().then(items => console.log(items));
     *
     @param {string} query - Optionaler Suchparameter
     @returns {Promise} Promise mit Array von Objekten
     */

    getItems: function (query) {

        return ajax('/items' + (query ? '?query=' + query : ''), {

            method: 'GET', headers: {
                'Accept': 'application/json'
            }
        });

    },

    /**
     ToDo: GET Request - Einzelne Ressource abrufen
     *
     Beispiel: service.getItem(42).then(item => console.log(item));
     *
     @param {string|number} id - ID der Ressource
     @returns {Promise} Promise mit Objekt
     */
    getItem: function (id) {

        return ajax('/items/' + id, {
            method: 'GET', headers: {
                'Accept': 'application/json'
            }
        });
    },

    /**
     ToDo: POST Request - Neue Ressource erstellen
     *
     Beispiel: service.createItem({name: 'Test'}).then(newItem => ...);
     *
     @param {Object} item - Das zu erstellende Objekt
     @returns {Promise} Promise mit erstelltem Objekt
     */
    createItem: function (item) {
        return ajax('/items', {
            method: 'POST', headers: {
                'Content-Type': 'application/json', 'Accept': 'application/json'
            }, body: JSON.stringify(item)
        });
    },

    /**
     ToDo: PUT Request - Ressource vollständig aktualisieren
     *
     Beispiel: service.updateItem(42, updatedData).then(item => ...);
     *
     @param {string|number} id - ID der Ressource
     @param {Object} item - Die aktualisierten Daten
     @returns {Promise} Promise mit aktualisiertem Objekt
     */
    updateItem: function (id, item) {
        return ajax('/items/' + id, {
            method: 'PUT', headers: {
                'Content-Type': 'application/json', 'Accept': 'application/json'
            }, body: JSON.stringify(item)
        });
    },

    /**
     ToDo: DELETE Request - Ressource löschen
     *
     Beispiel: service.deleteItem(42).then(() => console.log('Deleted'));
     *
     @param {string|number} id - ID der zu löschenden Ressource
     @returns {Promise} Promise (meist ohne Rückgabewert)
     */
    deleteItem: function (id) {
        return ajax('/items/' + id, {
            method: 'DELETE'
        });
    },

    /**
     ToDo: Mit Basic Authentication
     *
     Beispiel für geschützte Endpoints
     */
    getProtectedData: function (user) {
        return ajax('/protected/data', {
            method: 'GET', headers: {
                'Authorization': getAuthHeader(user), 'Accept': 'application/json'
            }
        });
    }

};

/**
 ToDo: Zentrale AJAX-Funktion mit Fetch API
 *
 Wichtig:
 - fetch() gibt Promise zurück
 - Promise rejected nur bei Netzwerkfehlern, NICHT bei HTTP-Fehler (404, 500)
 - Daher muss response.ok geprüft werden
 - JSON muss mit response.json() explizit geparsed werden
 *
 @param {string} path - API-Pfad (wird an BASE_URL angehängt)
 @param {Object} options - Fetch-Optionen (method, headers, body)
 @returns {Promise} Promise mit geparsten Daten oder rejected bei Fehler
 */
function ajax(path, options) {
    /* ToDo: Fetch gibt Promise zurück */
    return fetch(BASE_URL + path, options)
        .then(response => {
            /* ToDo: response.ok ist true für Status 200-299 */

            if (!response.ok) {
                /* ToDo: Bei HTTP-Fehler Promise rejecten */
                return Promise.reject(response);
            }
            /* ToDo: Prüfen ob Response JSON ist */
            const contentType = response.headers.get('Content-Type');

            if (contentType && contentType.includes('application/json')) {
                /* ToDo: JSON parsen und zurückgeben */
                return response.json();
            }

            /* ToDo: Bei 204 No Content oder DELETE gibt es oft keinen Body */
            return response;
        })
        .catch(error => {
            /* ToDo: Fehler-Handling error kann sein:
                - Response-Objekt bei HTTP-Fehler (404, 500, etc.)
                - Error-Objekt bei Netzwerkfehler
                - Error bei JSON-Parse-Fehler
            */
            console.error('AJAX Error:', error);
            return Promise.reject(error);
        });
}

/**
 ToDo: Basic Authentication Header erstellen
 *
 Format: "Basic " + base64(username:password)
 *
 @param {Object} user - Objekt mit name und password
 @returns {string} Authorization Header Wert
 */
function getAuthHeader(user) {
    /* ToDo: btoa() kodiert String in Base64 */
    return 'Basic ' + btoa(user.name + ':' + user.password);
}