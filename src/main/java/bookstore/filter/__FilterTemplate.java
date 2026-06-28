package bookstore.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

/**
 * TODO:
 * Allgemeine Vorlage für Servlet Filter.
 * <p>
 * Typische Einsatzzwecke:
 * - Logging
 * - Authentication
 * - Header-Prüfung
 * - Content-Type-Prüfung
 * - Request Blocking
 * <p>
 * Wichtiges Muster:
 * 1. Request/Response in HTTP-Typen casten
 * 2. Request vor dem Servlet prüfen
 * 3. Bei Fehler Response setzen und return verwenden
 * 4. Sonst chain.doFilter(req, resp) aufrufen
 * 5. Optional nach dem Servlet die Response auswerten
 * <p>
 * Hinweis zu zwei möglichen Varianten:
 * - implements Filter:      generisches Interface, Request/Response müssen manuell zu
 *                           HttpServletRequest/HttpServletResponse gecastet werden.
 * - extends HttpFilter:     HTTP-spezifische Basisklasse, HTTP-Typen bereits vorhanden,
 *                           kein Cast nötig. Empfohlen wenn nur HTTP-Requests verarbeitet werden.
 */
@WebFilter("/template/*") // TODO: URL-Mapping anpassen, z. B. "/api/*"
public class __FilterTemplate implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
            throws IOException, ServletException {

        /*
         * TODO:
         * ServletRequest und ServletResponse sind allgemeine Typen.
         * Für HTTP-spezifische Methoden wie getMethod(), getRequestURI()
         * oder setStatus() müssen sie zu HttpServletRequest und
         * HttpServletResponse gecastet werden.
         * Bei extends HttpFilter entfällt dieser Cast.
         */
        HttpServletRequest httpRequest = (HttpServletRequest) req;
        HttpServletResponse httpResponse = (HttpServletResponse) resp;

        /*
         * TODO:
         * Typische Request-Informationen lesen.
         * Diese Werte werden oft für Logging, Routing oder Prüfungen gebraucht.
         */
        String method = httpRequest.getMethod();
        String uri = httpRequest.getRequestURI();
        String contentType = httpRequest.getContentType();

        /*
         * TODO:
         * Einfaches Logging vor der eigentlichen Servlet-Verarbeitung.
         * In einem echten Projekt besser Logger statt System.out verwenden.
         */
        System.out.println(method + " " + uri);

        /*
         * TODO:
         * Beispiel für eine Content-Type-Prüfung.
         * POST/PUT/PATCH Requests mit Body sollten oft application/json senden.
         * Wenn die Prüfung fehlschlägt, wird der Request blockiert.
         */
        if (method.equals("POST") && contentType != null && !contentType.contains("application/json")) {

            httpResponse.setStatus(HttpServletResponse.SC_UNSUPPORTED_MEDIA_TYPE);
            httpResponse.getWriter().println("Content-Type application/json required");
            return;
        }

        /*
         * TODO:
         * Wichtigste Zeile eines Filters.
         * Ohne chain.doFilter(...) wird der Request nicht weitergegeben.
         * Das Servlet wird dann nicht ausgeführt.
         */
        chain.doFilter(req, resp);

        /*
         * TODO:
         * Code nach chain.doFilter(...) läuft nach dem Servlet.
         * Hier kann man z. B. den Response-Status loggen.
         */
        System.out.println("Response status: " + httpResponse.getStatus());
    }
}

/*
 * TODO:
 * Beispiel: AuthFilter – Login-Prüfung via HTTP Basic Auth
 * <p>
 * Bei Basic Auth gibt es keine Session. Der Browser schickt bei jedem Request
 * die Credentials im Authorization-Header mit (Base64-kodiert):
 *   Authorization: Basic dXNlcjpwYXNzd29yZA==
 * <p>
 * Statt den Login-Check in jedem Servlet manuell zu machen,
 * kann ein Filter alle geschützten Routen zentral absichern.
 * Das URL-Mapping bestimmt, welche Endpoints geschützt werden.
 * <p>
 * Vorteil gegenüber manueller Prüfung im Servlet:
 * - Single Responsibility: Servlet kümmert sich nur um die Logik
 * - Erweiterbar: neuer Endpoint schützen = nur Mapping anpassen
 *
 * @WebFilter("/api/orders/*")
 * public class AuthFilter implements Filter {
 *
 *     @Override
 *     public void doFilter(ServletRequest req, ServletResponse resp, FilterChain chain)
 *             throws IOException, ServletException {
 *
 *         HttpServletRequest httpRequest = (HttpServletRequest) req;
 *         HttpServletResponse httpResponse = (HttpServletResponse) resp;
 *
 *         // Authorization-Header lesen
 *         String authHeader = httpRequest.getHeader("Authorization");
 *
 *         // Fehlt der Header oder ist es kein Basic Auth → 401 + WWW-Authenticate-Header
 *         // WWW-Authenticate weist den Browser an, ein Login-Fenster anzuzeigen
 *         if (authHeader == null || !authHeader.startsWith("Basic ")) {
 *             httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 *             httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"bookstore\"");
 *             return;
 *         }
 *
 *         // Base64-Teil nach "Basic " dekodieren → "username:password"
 *         String decoded = new String(Base64.getDecoder().decode(authHeader.substring(6)));
 *         String[] parts = decoded.split(":", 2); // max. 2 Teile, Passwort darf ":" enthalten
 *         String username = parts[0];
 *         String password = parts[1];
 *
 *         // Credentials prüfen (in Produktion gegen DB oder LDAP, nie hardcoded)
 *         if (!username.equals("admin") || !password.equals("secret")) {
 *             httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
 *             httpResponse.setHeader("WWW-Authenticate", "Basic realm=\"bookstore\"");
 *             return;
 *         }
 *
 *         chain.doFilter(req, resp); // authentifiziert → Request weitergeben
 *     }
 * }
 */

