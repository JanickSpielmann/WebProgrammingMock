package bookstore.controller;

import bookstore.model.Book;
import bookstore.model.BookNotFoundException;
import bookstore.model.BookService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.io.PrintWriter;

/**
 * TODO:
 * Dieses Servlet zeigt das klassische MVC-Prinzip mit Servlets.
 * <p>
 * Controller:
 * - Diese Klasse nimmt HTTP Requests entgegen.
 * - Sie liest Request-Parameter aus.
 * - Sie entscheidet, welche Aktion ausgeführt wird.
 * - Sie bereitet Daten für die View vor.
 * <p>
 * Model:
 * - BookService und Book sind hier das Model.
 * - Der Controller soll die Daten nicht selber speichern.
 * - Fachlogik und Datenzugriff gehören nicht direkt in das Servlet.
 * <p>
 * View:
 * - In einem echten Projekt würde hier normalerweise eine Template Engine,
 * JSP oder eine HTML-Datei mit Platzhaltern verwendet.
 * - Damit diese Vorlage ohne zusätzliche Klassen direkt kompiliert und läuft,
 * wird die HTML-Ausgabe hier bewusst in kleine Render-Methoden ausgelagert.
 */

@WebServlet("/mvc-template")
public class __MvcServletTemplate extends HttpServlet {

    private static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";

    /**
     * TODO:
     * doGet wird typischerweise verwendet, um eine Seite anzuzeigen.
     * <p>
     * Beispiele:
     * - Formular anzeigen
     * - Suchresultate anzeigen
     * - Detailseite anzeigen
     * <p>
     * GET Requests sollen grundsätzlich keine Daten verändern.
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        String isbn = request.getParameter("isbn");

        /* TODO: Wenn kein ISBN-Parameter vorhanden, Suchformular anzeigen */
        if (isbn == null || isbn.isBlank()) {
            renderSearchForm(response, null, null);
            return;
        }

        try {
            Book book = BookService.getBook(isbn);

            /*
             TODO:
                In MVC werden Daten für die View vorbereitet.
                In vielen Projekten würde man diese Werte mit request.setAttribute(...)
                an eine Template Engine oder JSP weitergeben.
                Beispiel:
                    request.setAttribute("book", book);
                    request.getRequestDispatcher("/WEB-INF/templates/book.jsp").forward(request, response);
             */
            request.setAttribute("book", book);
            renderBookPage(response, book);
        } catch (BookNotFoundException ex) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            renderSearchForm(response, isbn, "Kein Buch mit dieser ISBN gefunden.");
        }
    }

    /**
     * TODO:
     * doPost wird typischerweise verwendet, wenn ein Formular abgeschickt wird.
     * <p>
     * Beispiele:
     * - Login
     * - Bestellung erstellen
     * - Formular validieren
     * - Danach entweder Seite erneut anzeigen oder weiterleiten
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        /* TODO: Request-Parameter auslesen und validieren */
        String isbn = request.getParameter("isbn");

        if (isbn == null || isbn.isBlank()) {

            /* TODO: Bei Validierungsfehler: 400 Bad Request und Formular erneut anzeigen */
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            renderSearchForm(response, isbn, "Die ISBN darf nicht leer sein.");
            return;
        }

        /*
         * TODO:
            Post-Redirect-Get Pattern:
            Nach einem erfolgreichen POST wird oft auf eine GET URL weitergeleitet.
            Dadurch wird verhindert, dass ein Browser-Refresh den POST nochmals sendet.
         */
        String target = request.getContextPath() + "/mvc-template?isbn=" + encodeSimple(isbn);
        response.sendRedirect(target);
    }

    /**
     * TODO:
     * Diese Methode stellt die View für das Suchformular dar.
     * <p>
     * In einer Prüfung ist wichtig:
     * - response.setContentType(...)
     * - PrintWriter über response.getWriter()
     * - HTML sauber und vollständig ausgeben
     * - Benutzerwerte nicht ungefiltert in HTML schreiben
     */
    private void renderSearchForm(HttpServletResponse response, String isbn, String errorMessage) throws IOException {

        response.setContentType(CONTENT_TYPE_HTML);

        try (PrintWriter out = response.getWriter()) {
            out.println("<!doctype html>");
            out.println("<html lang=\"de\">");
            out.println("<head>");
            out.println("  <meta charset=\"UTF-8\" />");
            out.println("  <title>MVC Servlet Template</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("  <h1>MVC Servlet Template</h1>");

            out.println("  <p>Dieses Beispiel verwendet ein Servlet als Controller (MVC-Pattern).</p>");

            /* TODO: Fehlermeldung anzeigen falls vorhanden */
            if (errorMessage != null) {
                out.println("  <p style=\"color: red;\">" + escapeHtml(errorMessage) + "</p>");
            }

            out.println("  <form action=\"mvc-template\" method=\"post\">");
            out.println("    <label for=\"isbn\">ISBN:</label>");
            out.println("    <input id=\"isbn\" name=\"isbn\" value=\"" + escapeHtml(valueOrEmpty(isbn)) + "\" />");
            out.println("    <button type=\"submit\">Buch suchen</button>");
            out.println("  </form>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /*
     * TODO:
     * Diese Methode stellt die View für ein gefundenes Buch dar.
     *
     * Der Controller hat vorher entschieden, welches Buch angezeigt wird.
     * Die View kümmert sich nur noch um die Darstellung.
     */
    private void renderBookPage(HttpServletResponse response, Book book) throws IOException {
        response.setContentType(CONTENT_TYPE_HTML);

        try (PrintWriter out = response.getWriter()) {
            out.println("<!doctype html>");
            out.println("<html lang=\"de\">");
            out.println("<head>");
            out.println("  <meta charset=\"UTF-8\" />");
            out.println("  <title>Buch gefunden</title>");
            out.println("</head>");
            out.println("<body>");
            out.println("  <h1>Buch gefunden</h1>");
            out.println("  <ul>");
            out.println("    <li>ISBN: " + escapeHtml(book.getIsbn()) + "</li>");
            out.println("    <li>Titel: " + escapeHtml(book.getTitle()) + "</li>");
            out.println("    <li>Autor: " + escapeHtml(book.getAuthor()) + "</li>");
            out.println("  </ul>");
            out.println("  <p><a href=\"mvc-template\">Zurück zur Suche</a></p>");
            out.println("</body>");
            out.println("</html>");
        }
    }

    /*
     * TODO:
     * Kleine Hilfsmethode für Formularwerte.
     * Dadurch muss die View nicht überall null prüfen.
     */
    private String valueOrEmpty(String value) {
        return value == null ? "" : value;
    }

    /*
     * TODO:
     * Minimales HTML Escaping.
     *
     * Wichtig:
     * Werte aus Requests oder dem Model sollten nicht ungefiltert in HTML landen.
     * Sonst entsteht schnell ein XSS-Problem.
     */
    private String escapeHtml(String value) {
        return valueOrEmpty(value).replace("&", "&amp;")
                .replace("<", "&lt;")
                .replace(">", "&gt;")
                .replace("\"", "&quot;");
    }

    /*
     * TODO:
     * Vereinfachtes URL Encoding für diese Vorlage.
     *
     * Für produktiven Code wäre URLEncoder.encode(value, StandardCharsets.UTF_8)
     * sauberer. Hier bleibt es absichtlich minimal und gut lesbar.
     */
    private String encodeSimple(String value) {
        return value.trim().replace(" ", "+");
    }
}
