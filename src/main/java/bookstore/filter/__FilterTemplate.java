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
 * 4. Sonst chain.doFilter(request, response) aufrufen
 * 5. Optional nach dem Servlet die Response auswerten
 */
@WebFilter("/template/*") // TODO: URL-Mapping anpassen, z. B. "/api/*"
public class __FilterTemplate implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        /**
         * TODO:
         * ServletRequest und ServletResponse sind allgemeine Typen.
         * Für HTTP-spezifische Methoden wie getMethod(), getRequestURI()
         * oder setStatus() müssen sie zu HttpServletRequest und
         * HttpServletResponse gecastet werden.
         */
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

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
        chain.doFilter(request, response);

        /*
         * TODO:
         * Code nach chain.doFilter(...) läuft nach dem Servlet.
         * Hier kann man z. B. den Response-Status loggen.
         */
        System.out.println("Response status: " + httpResponse.getStatus());
    }
}

