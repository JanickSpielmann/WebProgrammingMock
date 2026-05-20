/**
 ToDo: Minimale Main.js - Kurz und übersichtlich
 *
 Diese Variante konzentriert sich auf das Wesentliche:
 - Router importieren
 - Components importieren
 - Routes registrieren
 - Initiale Navigation
 */

import router from './router.js';
import home from './components/home.js';
import bookDetail from './components/book-detail.js';
import order from './components/order.js';

/* ToDo: Routes mit Components verknüpfen */
router.register('/', home);
router.register('/book', bookDetail);
router.register('/order', order);

/* ToDo: App starten - Navigation zur aktuellen oder Default-Route */
router.navigate(location.hash.substring(1) || '/');