const BASE_URL = '/api';

export default {
    getBooks: function (query) {
        return ajax('/books' + (query ? '?query=' + query : ''), {
            method: 'GET',
            headers: {'Accept': 'application/json'}
        });
    },


    placeOrder: function (order) {
        return ajax('/orders', {
            method: 'POST',
            headers: {'Accept': 'application/json', 'Content-Type': 'application/json'},
            body: JSON.stringify(order)
        });
    }
};

function ajax(path, options) {
    return fetch(BASE_URL + path, options)
        .then(response => {
            if (!response.ok) throw response;
            return response.headers.get('Content-Type') === 'application/json' ? response.json() : response;
        })
}
