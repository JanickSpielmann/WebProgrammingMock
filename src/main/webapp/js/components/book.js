import util from '../util.js';
import router from '../router.js';
import store from '../store.js';

export default {



    title: 'Book',
    render: async function (isbn) {
        const view = await util.loadTemplate('book.html');
        const bookTitle = view.querySelector('.title');
        const bookSubtitle = view.querySelector('.subtitle');
        const book = store.getBook(isbn)
        util.interpolate('book', book, view)
        view.querySelector('.back').onclick = () => router.navigate('/');
        view.querySelector('img').src = book.image;
        fetch('https://locher.ti.bfh.ch/services/translate?text=' + book.title)
            .then(r => r.text())
            .then(translated => {
                view.querySelector('.loading-title').style.display = 'none';
                bookTitle.style.display = '';
                bookTitle.textContent = translated;
            })
            .catch(() => {
                view.querySelector('.loading-title').style.display = 'none';
                bookTitle.style.display = '';
                bookTitle.style.fontStyle = 'italic';
            });

        fetch('https://locher.ti.bfh.ch/services/translate?text=' + book.subtitle)
            .then(r => r.text())
            .then(translated => {
                view.querySelector('.loading-subtitle').style.display = 'none';
                bookSubtitle.style.display = '';
                bookSubtitle.textContent = translated;
            })
            .catch(() => {
                view.querySelector('.loading-subtitle').style.display = 'none';
                bookSubtitle.style.display = '';
                bookSubtitle.style.fontStyle = 'italic';
            });

        return view;
    }
};