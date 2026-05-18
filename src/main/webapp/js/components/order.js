import router from '../router.js';
import util from '../util.js';

export default {
	title: 'Order',
	render: async function(isbn) {
		const view = await util.loadTemplate('order.html');
		const form = view.querySelector('form');
		form.onsubmit = () => false;
		const order = {isbn};
		util.bindForm(form, order);
		form.cancel.onclick = () => router.navigate('/');
		return view;
	}
};
