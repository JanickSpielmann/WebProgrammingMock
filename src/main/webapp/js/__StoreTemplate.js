/**
 ToDo: Store für Application State Management
 *
 Einsatzzweck:
 - Zentrale Datenhaltung für die gesamte App
 - Teilen von Daten zwischen Components
 - Vermeidet redundante AJAX-Requests
 *
 Wichtige Konzepte:
 - Closures für private Daten
 - Getter/Setter Pattern
 - Data Consistency
 */

/* ToDo: Private Daten in Closure
let ermöglicht Reassignment für clear()
*/
let data = {};

export default {
    /* ToDo: User Getter/Setter */
    setUser: function (user) {
        data.user = user;
    },
    getUser: function () {
        return data.user;
    },

    /* ToDo: Items Getter/Setter */
    setItems: function (items) {

        /* ToDo: Array sortieren für konsistente Reihenfolge */
        data.items = items.sort((a, b) => a.name.localeCompare(b.name));
    },

    getItems: function () {
        return data.items;
    },

    /* ToDo: Einzelnes Item finden */
    getItem: function (id) {

        /* ToDo: find() gibt erstes Match oder undefined zurück */
        return data.items ? data.items.find(item => item.id === Number(id)) : undefined;
    },

    /* ToDo: Item hinzufügen */
    addItem: function (item) {

        if (!data.items) {
            data.items = [];
        }
        data.items.push(item);

        /* ToDo: Array nach Hinzufügen neu sortieren */
        data.items = data.items.sort((a, b) => a.name.localeCompare(b.name));
    },

    /* ToDo: Item aktualisieren */
    updateItem: function (updatedItem) {
        const item = this.getItem(updatedItem.id);
        if (item) {

            /* ToDo: Object.assign() überschreibt Properties */
            Object.assign(item, updatedItem);
        }
    },

    /* ToDo: Item entfernen */
    removeItem: function (id) {
        if (!data.items) return;

        /* ToDo: Index des Items finden */
        const index = data.items.findIndex(item => item.id === Number(id));

        /* ToDo: Item aus Array entfernen falls gefunden */
        if (index >= 0) {
            data.items.splice(index, 1);
        }
    },

    /* ToDo: Store komplett leeren (z.B. bei Logout) */
    clear: function () {
        data = {};
    },

    /* ToDo: Store in LocalStorage persistieren (optional) */
    save: function () {
        try {
            localStorage.setItem('appData', JSON.stringify(data));
        } catch (error) {
            console.error('Failed to save to localStorage:', error);
        }
    },

    /* ToDo: Store aus LocalStorage laden (optional) */
    load: function () {
        try {
            const stored = localStorage.getItem('appData');
            if (stored) {
                data = JSON.parse(stored);
            }
        } catch (error) {
            console.error('Failed to load from localStorage:', error);
        }
    }
};