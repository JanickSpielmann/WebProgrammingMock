import router from '../router.js';
import util from '../util.js';
import service from '../service.js';

export default {
    title: 'Order',
    render: async function (isbn) {
        const view = await util.loadTemplate('order.html');
        const form = view.querySelector('form');
        const order = {isbn};
        util.bindForm(form, order);
        form.cancel.onclick = () => router.navigate('/');

        form.onsubmit = (event) => {
            event.preventDefault();
            service.placeOrder(order)
                .then(() => {
                    form.style.display = 'none';
                    const msg = document.createElement('p');
                    msg.textContent = 'Ihre Bestellung wurde erfolgreich aufgegeben.';
                    view.appendChild(msg);
                })
                .catch(() => {
                    form.style.display = 'none';
                    const msg = document.createElement('p');
                    msg.textContent = 'Fehler beim Aufgeben der Bestellung. Bitte versuchen Sie es erneut.';
                    view.appendChild(msg);
                });
        };

        return view;
    }
};
