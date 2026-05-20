/**
 ToDo: Utility-Funktionen für SPA

 *
 Einsatzzweck:
 - Wiederverwendbare Helper-Funktionen
 - Template Loading
 - Data Binding
 - DOM Manipulation
 */

/* ToDo: Pfad zu HTML-Templates */
const TEMPLATE_ROOT = '/templates/';

export default {
    /**
     ToDo: HTML-Template laden
     *
     @param {string} template - Dateiname des Templates
     @param {string} tag - HTML-Tag für Root-Element (default: 'div')
     @returns {Promise<HTMLElement>} Promise mit geladenem Template
     */
    loadTemplate: function (template, tag) {

        /* ToDo: Container-Element erstellen */
        const el = document.createElement(tag || 'div');

        /* ToDo: Template-Datei via Fetch laden */
        return fetch(TEMPLATE_ROOT + template)
            .then(response => {
                if (!response.ok) {
                    return Promise.reject(response);
                }
                return response.text();
            })
            .then(html => {

                /* ToDo: HTML in Element einfügen */
                el.innerHTML = html;
                return el;
            })
            .catch(error => {
                return Promise.reject(`Loading template '${template}' failed!`);
            });
    },

    /**
     ToDo: Daten in Template interpolieren
     *
     Sucht Elemente mit data-field Attribut und setzt deren Inhalt
     *
     @param {string} key - Property-Name
     @param {*} value - Wert (kann Objekt, Array, String, etc. sein)
     @param {HTMLElement} context - Root-Element (default: document)
     */
    interpolate: function (key, value, context = document) {

        /* ToDo: Verschachtelte Objekte rekursiv verarbeiten */
        if (value instanceof Object && !Array.isArray(value)) {
            for (let prop in value) {
                this.interpolate(`${key}.${prop}`, value[prop], context);
            }
        } else {

            /* ToDo: Alle Elemente mit data-field="key" finden und befüllen */
            context.querySelectorAll(`[data-field="${key}"]`).forEach(el => {
                el.innerHTML = value ?? '';
            });
        }
    },

    /**
     ToDo: Two-Way Data Binding für Formulare
     *
     Bindet Formular-Felder an Objekt-Properties
     *
     @param {HTMLFormElement} form - Das Formular
     @param {Object} obj - Das zu bindende Objekt
     */
    bindForm: function (form, obj) {

        /* ToDo: Alle eindeutigen Feldnamen extrahieren */
        const names = [...new Set(Array.from(form.elements).map(el => el.name))];

        /* ToDo: Für jeden Feldnamen Binding erstellen */
        names.forEach(name => {
            if (name) {
                bindFormField(form[name], obj, name);
            }
        });
    }
};

/**
 ToDo: Private Helper - Einzelnes Formular-Feld binden
 *
 @param {HTMLElement|RadioNodeList} elem - Formular-Element(e)
 @param {Object} obj - Objekt
 @param {string} prop - Property-Name
 */
function bindFormField(elem, obj, prop) {

    /* ToDo: RadioNodeList (mehrere Elemente mit gleichem Namen) */
    if (elem instanceof RadioNodeList) {
        elem.forEach(el => {
            if (el.type === 'radio') {

                /* ToDo: Radio Button */
                el.checked = el.value == obj[prop];
                el.oninput = () => obj[prop] = el.value;
            } else if (el.type === 'checkbox') {

                /* ToDo: Checkbox - Array von Werten */
                el.checked = obj[prop] && obj[prop].includes && obj[prop].includes(el.value);
                el.oninput = () => {
                    obj[prop] = Array.from(elem).filter(e => e.checked).map(e => e.value);
                };
            } else {

                /* ToDo: Normale Eingabefelder */
                bindFormField(el, obj, prop);
            }

        });
    } else {

        /* ToDo: Einzelnes Eingabefeld binden */
        elem.value = obj[prop] ?? '';
        elem.oninput = () => {
            obj[prop] = elem.type === 'number' ? Number(elem.value) : elem.value;
        };
    }
}