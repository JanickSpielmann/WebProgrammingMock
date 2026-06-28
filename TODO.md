# Backend

## REST Servlet
1. Klasse erstellen: `@WebServlet("/api/xyz/*") public class XyzServlet extends HttpServlet`
2. `doGet` — alle zurückgeben:
   ```java
   resp.setContentType("application/json;charset=UTF-8");
   objectMapper.writeValue(resp.getOutputStream(), XyzService.getAll());
   ```
3. `doPost` — aus JSON Body lesen, validieren, Service aufrufen:
   ```java
   Xyz xyz = objectMapper.readValue(req.getInputStream(), Xyz.class);
   if (xyz.getField() == null) { resp.setStatus(SC_BAD_REQUEST); return; }
   XyzService.add(xyz);
   resp.setStatus(SC_CREATED);
   ```
4. `doDelete` — ID aus Pfad lesen, Service aufrufen:
   ```java
   String pathInfo = req.getPathInfo();
   if (pathInfo == null) { resp.setStatus(SC_BAD_REQUEST); return; }
   XyzService.remove(Integer.parseInt(pathInfo.substring(1)));
   resp.setStatus(SC_OK);
   ```
   Exceptions catchen: `NumberFormatException → 400`, `NotFoundException → 404`

## Filter (Basic Auth)
1. Klasse erstellen: `@WebFilter("/api/xyz/*") public class AuthFilter extends HttpFilter`
2. Methode prüfen: `String method = req.getMethod();`
3. Nur für geschützte Methoden prüfen (z.B. GET, DELETE):
   ```java
   if (method.equals("GET") || method.equals("DELETE")) {
       String authHeader = req.getHeader("Authorization");
       if (authHeader == null || !authHeader.startsWith("Basic ")) {
           resp.setStatus(SC_UNAUTHORIZED);
           resp.setHeader("WWW-Authenticate", "Basic realm=\"app\"");
           return;
       }
       String decoded = new String(Base64.getDecoder().decode(authHeader.substring(6)));
       String[] parts = decoded.split(":", 2);
       if (!parts[0].equals("admin") || !parts[1].equals("12345")) {
           resp.setStatus(SC_UNAUTHORIZED);
           return;
       }
   }
   chain.doFilter(req, resp);
   ```

---

# Frontend

## Neue Komponente
1. `main.js` — import + Route registrieren:
   ```js
   import xyz from './components/xyz.js';
   router.register('/xyz', xyz);
   ```
2. Aufrufende Component — Klick-Handler ergänzen:
   ```js
   el.addEventListener('click', () => router.navigate('/xyz', param));
   ```
3. `components/xyz.js` erstellen:
   ```js
   import util from '../util.js';
   import store from '../store.js';
   import router from '../router.js';

   export default {
       title: 'Xyz',
       render: async function (param) {
           const view = await util.loadTemplate('xyz.html');
           const obj = store.getObj(param);
           util.interpolate('obj', obj, view);
           view.querySelector('.back').onclick = () => router.navigate('/');
           return view;
       }
   };
   ```
4. `templates/xyz.html` erstellen — `data-field="obj.prop"` für alle Felder:
   ```html
   <h2 data-field="obj.title"></h2>
   <p data-field="obj.description"></p>
   <a class="back">« Back</a>
   ```

## AJAX / Fetch (service.js)
1. Funktion in `service.js` ergänzen:
   ```js
   placeOrder: function(obj) {
       return ajax('/xyz', {
           method: 'POST',
           headers: { 'Accept': 'application/json', 'Content-Type': 'application/json' },
           body: JSON.stringify(obj)
       });
   }
   ```
2. In Component `onsubmit` Handler:
   ```js
   form.onsubmit = (event) => {
       event.preventDefault();
       service.placeOrder(obj)
           .then(() => {
               form.style.display = 'none';
               // Bestätigung anzeigen
           })
           .catch(() => {
               form.style.display = 'none';
               // Fehlermeldung anzeigen
           });
   };
   ```

## Externer Service (z.B. Übersetzen)
1. Loading-Label im HTML:
   ```html
   <span class="loading-title">Loading...</span>
   <span class="title" style="display:none"></span>
   ```
2. In Component fetch aufrufen:
   ```js
   fetch('https://service.url?text=' + obj.title)
       .then(r => r.text())
       .then(translated => {
           view.querySelector('.loading-title').style.display = 'none';
           view.querySelector('.title').style.display = '';
           view.querySelector('.title').textContent = translated;
       })
       .catch(() => {
           view.querySelector('.loading-title').style.display = 'none';
           view.querySelector('.title').style.display = '';
           view.querySelector('.title').style.fontStyle = 'italic';
       });
   ```

---

# Allgemein
- **Bootstrap** — `index.html`: `<link rel="stylesheet" href="https://cdn.jsdelivr.net/npm/bootstrap@5.3.0/dist/css/bootstrap.min.css">`
- **Hard Refresh** — `Ctrl+Shift+R` nach JS-Änderungen
- **`return` nach Fehler** — immer `return` nach `resp.setStatus(...)` im Servlet sonst läuft Code weiter
- **Bild** — `util.interpolate` setzt `innerHTML`, nicht `src` → Bild manuell setzen: `view.querySelector('img').src = obj.image`
