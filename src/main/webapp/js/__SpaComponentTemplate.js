/**
 ToDo: Template für SPA Component
 *
 Einsatzzweck:
 - Stellt eine View/Seite der Applikation dar
 - Kapselt HTML-Template, Daten und Logik
 - Wird vom Router gerendert
 *
 Wichtige Konzepte:
 - Component als Objekt mit render() Methode
 - Trennung Public/Private Code
 - Template Loading
 - Event Handler Registration
 - DOM Manipulation
 - Integration mit Service und Store
 */

/* ToDo: Imports von anderen Modulen von .js !!!!! */
import util from '../util.js';
import service from '../service.js';
import store from '../store.js';
import router from '../router.js';


/**
 ToDo: Public Component Interface
 *
 Wird exportiert und vom Router verwendet
 */
export default {
    /* ToDo: Titel der Component (für Browser-Titel) */
    title: 'My Component', /**
     ToDo: Render-Methode - Erstellt und returned die View
     *
     Ablauf:
     1. HTML-Template laden
     2. Daten abrufen (vom Store oder Service)
     3. View mit Daten befüllen
     4. Event Handler registrieren
     5. View zurückgeben
     *
     @param {string} param - Optionaler URL-Parameter
     @returns {Promise<HTMLElement>} Promise mit Root-Element der View
     */
    render: async function (param) {
        /* ToDo: 1. Template laden
        util.loadTemplate() lädt HTML-Datei und gibt Element zurück
        */
        const view = await util.loadTemplate('my-component.html');

        /* ToDo: 2. Daten abrufen
        Option A: Aus Store (für bereits geladene Daten)
        */

        let items = store.getItems();
        /* ToDo: Option B: Vom Service (bei erstem Laden) */
        if (!items) {
            try {
                items = await service.getItems();

                /* ToDo: Daten im Store für spätere Verwendung speichern */
                store.setItems(items);
            } catch (error) {

                /* ToDo: Error Handling bei AJAX-Fehler */
                console.error('Failed to load items:', error);
                view.innerHTML = '<p class="error">Error loading data</p>';
                return view;
            }
        }

        /* ToDo: 3. View mit Daten befüllen
        Jedes Item wird gerendert und in die Liste eingefügt
        */
        items.forEach(item => renderItem(view, item));

        /* ToDo: 4. Event Handler registrieren
        Beispiel: Button-Click Handler
        */
        const addButton = view.querySelector('[data-action="add"]');

        if (addButton) {
            addButton.addEventListener('click', event => {

                /* ToDo: Event Handler Logik */
                event.preventDefault();
                handleAddClick(view);
            });
        }

        /* ToDo: Beispiel: Form Submit Handler */
        const form = view.querySelector('form');
        if (form) {
            form.addEventListener('submit', event => {
                event.preventDefault();
                handleFormSubmit(form, view);
            });
        }

        /* ToDo: Beispiel: Navigation zu anderer Component */
        view.querySelectorAll('[data-item-id]').forEach(element => {

            element.addEventListener('click', event => {
                const id = event.currentTarget.dataset.itemId;
                router.navigate('/detail', id);
            });
        });

        /* ToDo: 5. View zurückgeben */
        return view;
    }
};

/**
 ToDo: Private Helper-Funktion - Item rendern
 *
 Erstellt HTML für ein einzelnes Item und fügt es in die View ein
 *
 @param {HTMLElement} view - Root-Element der View
 @param {Object} item - Daten-Objekt
 */
function renderItem(view, item) {

    /* ToDo: HTML-Template als String
    Template Literals (`) ermöglichen mehrzeilige Strings und Interpolation
    */
    const template = `
<li data-item-id="${item.id}">
    <h3>${escapeHtml(item.name)}</h3>
    <p>${escapeHtml(item.description)}</p>
    <span class="price">CHF ${item.price.toFixed(2)}</span>
    <button class="btn-delete" data-id="${item.id}">Delete</button>
</li>
`;

    /* ToDo: Li-Element erstellen */
    const li = document.createElement('li');

    li.innerHTML = template;

    /* ToDo: Event Handler für Delete-Button */
    const deleteBtn = li.querySelector('.btn-delete');
    deleteBtn.addEventListener('click', event => {
        event.stopPropagation(); // Verhindert Event Bubbling
        handleDeleteClick(item.id, li);
    });

    /* ToDo: Element in Liste einfügen */
    view.querySelector('ul').append(li);
}

/**
 ToDo: Private Helper-Funktion - Add-Click Handler
 *
 @param {HTMLElement} view - Root-Element der View
 */
function handleAddClick(view) {

    /* ToDo: Navigation zu Add-Formular */
    router.navigate('/add');
}

/**
 ToDo: Private Helper-Funktion - Form Submit Handler
 *
 @param {HTMLFormElement} form - Das Formular
 @param {HTMLElement} view - Root-Element der View
 */
async function handleFormSubmit(form, view) {

    /* ToDo: FormData aus Formular extrahieren */
    const formData = new FormData(form);

    /* ToDo: Objekt aus FormData erstellen */
    const item = {

        name: formData.get('name'), description: formData.get('description'), price: Number(formData.get('price'))
    };

    /* ToDo: Validierung */
    if (!item.name || item.price <= 0) {
        alert('Please fill all required fields');
        return;
    }
    try {

        /* ToDo: Item via Service erstellen */
        const newItem = await service.createItem(item);

        /* ToDo: Store aktualisieren */
        store.addItem(newItem);

        /* ToDo: Zurück zur Liste navigieren */
        router.navigate('/list');

    } catch (error) {

        /* ToDo: Error Handling */
        console.error('Failed to create item:', error);
        alert('Error creating item. Please try again.');
    }
}

/**
 ToDo: Private Helper-Funktion - Delete-Click Handler
 *
 @param {string|number} id - ID des zu löschenden Items
 @param {HTMLElement} element - DOM-Element des Items
 */
async function handleDeleteClick(id, element) {

    /* ToDo: Bestätigung vom User */
    if (!confirm('Are you sure you want to delete this item?')) {
        return;
    }
    try {

        /* ToDo: Item via Service löschen */
        await service.deleteItem(id);

        /* ToDo: Aus Store entfernen */
        store.removeItem(id);

        /* ToDo: Aus DOM entfernen */
        element.remove();
    } catch (error) {

        /* ToDo: Error Handling */
        console.error('Failed to delete item:', error);
        alert('Error deleting item. Please try again.');
    }

}

/**
 ToDo: Private Helper-Funktion - HTML escapen
 *
 Verhindert XSS-Angriffe durch Escapen von HTML-Sonderzeichen
 *
 @param {string} text - Zu escapender Text
 @returns {string} Escapeter Text
 */
function escapeHtml(text) {
    if (!text) return '';
    return text.toString()
        .replace(/&/g, '&amp;')
        .replace(/</g, '&lt;')
        .replace(/>/g, '&gt;')
        .replace(/"/g, '&quot;')
        .replace(/'/g, '&#039;');
}

/**
 ToDo: Alternative - Daten-Interpolation mit util.interpolate()
 *
 Falls util.js eine interpolate-Funktion bereitstellt:
 */
function renderItemAlternative(view, item) {
    const template = `
<li data-field-container>
    <h3 data-field="item.name"></h3>
    <p data-field="item.description"></p>
    <span data-field="item.price"></span>
</li>
`;

    const li = document.createElement('li');
    li.innerHTML = template;

    /* ToDo: Daten interpolieren */
    util.interpolate('item', item, li);
    view.querySelector('ul').append(li);
}