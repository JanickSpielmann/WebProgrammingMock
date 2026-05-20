import util from '../util.js';
import service from '../service.js';
import store from '../store.js';
import router from '../router.js';

/**
 TODO: List Component Template
 *
 Dieses Template zeigt eine typische Liste von Elementen an.
 *
 Wichtige Konzepte:
 - Template laden mit util.loadTemplate()
 - Daten vom Service holen
 - Daten im Store speichern
 - Dynamisch Liste rendern
 - Event Handler für Buttons registrieren
 - Navigation mit router.navigate()
 */
export default {

    title: 'Item List', render: async function () {

        /* TODO: Template laden */
        const view = await util.loadTemplate('list-template.html');

        /* TODO: Prüfen ob Daten bereits im Store vorhanden sind */
        if (!store.getItems()) {

            /* TODO: Daten vom Service holen und im Store speichern */
            service.getItems().then(items => {
                store.setItems(items);
                items.forEach(item => renderItem(view, item));
            }).catch(error => {

                /* TODO: Fehlerbehandlung */
                view.querySelector('.error').textContent = 'Fehler beim Laden der Daten';
                console.error(error);
            });
        } else {

            /* TODO: Daten aus Store holen und rendern */
            store.getItems().forEach(item => renderItem(view, item));
        }

        /* TODO: Event Handler für "Neues Element hinzufügen" Button */
        view.querySelector('[data-action="add"]').addEventListener('click', () => {
            router.navigate('/add-item');
        });

        /* TODO: Event Handler für Filter/Suche */
        view.querySelector('[data-action="search"]').addEventListener('input', (e) => {
            const query = e.target.value.toLowerCase();
            const filteredItems = store.getItems().filter(item => item.title.toLowerCase().includes(query));
            view.querySelector('ul').innerHTML = '';
            filteredItems.forEach(item => renderItem(view, item));
        });
        return view;
    }
};

/**
 TODO: Private Hilfsfunktion zum Rendern eines einzelnen List-Items
 */
function renderItem(view, item) {

    /* TODO: List Item Element erstellen */
    const li = document.createElement('li');
    li.innerHTML = `
<div class="item-content">
    <h3>${item.title}</h3>
    <p>${item.description || ''}</p>
    <span class="item-meta">${item.date || ''}</span>
</div>
<div class="item-actions">
    <button class="btn-edit" data-id="${item.id}">Edit</button>
    <button class="btn-delete" data-id="${item.id}">Delete</button>
</div>
`;

    /* TODO: Event Handler für Edit Button */
    li.querySelector('.btn-edit').addEventListener('click', () => {
        router.navigate('/edit-item', item.id);
    });

    /* TODO: Event Handler für Delete Button */
    li.querySelector('.btn-delete').addEventListener('click', () => {
        if (confirm('Wirklich löschen?')) {
            service.deleteItem(item.id).then(() => {
                store.removeItem(item.id);
                li.remove();
            }).catch(error => {
                alert('Fehler beim Löschen');
                console.error(error);
            });
        }
    });

    /* TODO: List Item zur Liste hinzufügen */
    view.querySelector('ul').append(li);
}