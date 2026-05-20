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
 * TODO: Dieses Servlet zeigt das klassische MVC-Prinzip mit Servlets.
 * <p>
 * <p>
 * <p>
 * Model-View-Controller (MVC):
 * <p>
 * <p>
 * <p>
 * Controller (diese Klasse):
 * <p>
 * - Nimmt HTTP Requests entgegen
 * <p>
 * - Liest Request-Parameter aus
 * <p>
 * - Entscheidet, welche Aktion ausgeführt wird
 * <p>
 * - Ruft Model-Methoden auf (z.B. BookService)
 * <p>
 * - Bereitet Daten für die View vor
 * <p>
 * - Wählt passende View aus
 * <p>
 * <p>
 * <p>
 * Model (BookService, Book):
 * <p>
 * - Enthält die Geschäftslogik
 * <p>
 * - Verwaltet Daten und Datenzugriff
 * <p>
 * - Ist unabhängig von View und Controller
 * <p>
 * <p>
 * <p>
 * View (hier: render-Methoden):
 * <p>
 * - Stellt Daten dar (HTML)
 * <p>
 * - Sollte keine Logik enthalten
 * <p>
 * - In echten Projekten: JSP, Thymeleaf, Freemarker, etc.
 */

@WebServlet("/mvc-template")

public class __MvcServletTemplate extends HttpServlet {

    private static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";

    /**
     * TODO: doGet wird typischerweise verwendet, um eine Seite anzuzeigen.
     * <p>
     * <p>
     * <p>
     * Typische Anwendungsfälle:
     * <p>
     * - Formular anzeigen (z.B. Suchformular, Login-Formular)
     * <p>
     * - Suchresultate anzeigen
     * <p>
     * - Detailseite anzeigen
     * <p>
     * - Übersichtsseite (Dashboard) anzeigen
     * <p>
     * <p>
     * <p>
     * REST-Prinzip: GET Requests sollen grundsätzlich keine Daten verändern (safe).
     */

    @Override

    protected void doGet(HttpServletRequest request, HttpServletResponse response)

            throws IOException {

/* TODO: Request-Parameter auslesen.

Parameter können sein:

- Query-Parameter: /mvc-template?isbn=978-3-86490-801-9

- Form-Parameter: aus einem HTML-Formular (bei GET als Query-String) */

        String isbn = request.getParameter("isbn");

        /* TODO: Wenn kein ISBN-Parameter vorhanden, Suchformular anzeigen */

        if (isbn == null || isbn.isBlank()) {

            renderSearchForm(response, null, null);

            return;

        }

        /* TODO: Mit ISBN nach Buch suchen (Model-Aufruf) */

        try {

            Book book = BookService.getBook(isbn);

/* TODO: In echtem MVC werden Daten für die View als Request-Attribute gesetzt.

Dann wird die Request an eine Template Engine oder JSP weitergeleitet:

*

request.setAttribute("book", book);

request.getRequestDispatcher("/WEB-INF/views/book.jsp")

.forward(request, response);

*

Vorteil: Trennung von Controller-Logik und View-Code.

In dieser Vorlage wird zur Einfachheit direkt HTML generiert. */

            request.setAttribute("book", book);

            renderBookPage(response, book);

        } catch (BookNotFoundException ex) {

            /* TODO: Bei nicht gefundenem Buch: 404 Status setzen und Fehlermeldung anzeigen */

            response.setStatus(HttpServletResponse.SC_NOT_FOUND);

            renderSearchForm(response, isbn, "Kein Buch mit dieser ISBN gefunden.");

        }

    }

    /**
     * TODO: doPost wird typischerweise verwendet, wenn ein Formular abgeschickt wird.
     * <p>
     * <p>
     * <p>
     * Typische Anwendungsfälle:
     * <p>
     * - Login-Formular absenden
     * <p>
     * - Bestellung erstellen
     * <p>
     * - Daten speichern
     * <p>
     * - Formular validieren
     * <p>
     * <p>
     * <p>
     * POST ist nicht safe (kann Daten verändern) und nicht idempotent.
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

/* TODO: Post-Redirect-Get (PRG) Pattern:

Nach einem erfolgreichen POST wird oft auf eine GET URL weitergeleitet.

*

Vorteile:

- Browser-Refresh sendet nicht erneut den POST

- Verhindert Doppel-Submissions

- Bessere User Experience

- Sauberere URL (ohne POST-Daten)

*

sendRedirect sendet 302/303 Status und Location-Header an den Browser.

Browser macht dann automatisch einen neuen GET Request. */

        String target = request.getContextPath() + "/mvc-template?isbn=" + encodeSimple(isbn);

        response.sendRedirect(target);

    }

    /**
     * TODO: View-Methode für das Suchformular.
     * <p>
     * <p>
     * <p>
     * Wichtige Punkte für die Prüfung:
     * <p>
     * - response.setContentType(...) setzen
     * <p>
     * - PrintWriter über response.getWriter() holen
     * <p>
     * - HTML sauber und vollständig ausgeben (<!doctype html>, <html>, <head>, <body>)
     * <p>
     * - Benutzerwerte IMMER escapen (escapeHtml) gegen XSS
     * <p>
     * - Formular-Action und -Method korrekt setzen
     */

    private void renderSearchForm(HttpServletResponse response, String isbn, String errorMessage)

            throws IOException {

        response.setContentType(CONTENT_TYPE_HTML);

        try (PrintWriter out = response.getWriter()) {

            out.println("<!doctype html>");

            out.println("<html lang=\"de\">");

            out.println("<head>");

            out.println("  <meta charset=\"UTF-8\" />");

            out.println("  <title>MVC Servlet Template - Buchsuche</title>");

            out.println("  <style>");

            out.println("    body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; }");

            out.println("    .error { color: red; background: #fee; padding: 10px; border-radius: 4px; }");

            out.println("    input { padding: 8px; width: 300px; }");

            out.println("    button { padding: 8px 16px; background: #007bff; color: white; border: none; cursor: " + "pointer; }");

            out.println("  </style>");

            out.println("</head>");

            out.println("<body>");

            out.println("  <h1>MVC Servlet Template</h1>");

            out.println("  <p>Dieses Beispiel verwendet ein Servlet als Controller (MVC-Pattern).</p>");

            /* TODO: Fehlermeldung anzeigen falls vorhanden */

            if (errorMessage != null) {

                out.println("  <div class=\"error\">" + escapeHtml(errorMessage) + "</div>");

            }

/* TODO: Formular ausgeben.

- action: Ziel-URL (relativ zum Context-Root)

- method: POST (nicht GET, da Suche eine Aktion ist)

- label + input für Barrierefreiheit

- value vorbelegen falls isbn vorhanden */

            out.println("  <form action=\"mvc-template\" method=\"post\">");

            out.println("    <label for=\"isbn\">ISBN:</label><br>");

            out.println("    <input id=\"isbn\" name=\"isbn\" " +

                                "value=\"" + escapeHtml(valueOrEmpty(isbn)) + "\" " +

                                "placeholder=\"978-3-86490-801-9\" />");

            out.println("    <button type=\"submit\">Buch suchen</button>");

            out.println("  </form>");

            out.println("  <hr>");

            out.println("  <p><small>Beispiel-ISBN: 978-3-86490-801-9</small></p>");

            out.println("</body>");

            out.println("</html>");

        }

    }

    /**
     * TODO: View-Methode für die Detailseite eines Buchs.
     * <p>
     * <p>
     * <p>
     * Der Controller hat vorher entschieden, welches Buch angezeigt wird.
     * <p>
     * Die View kümmert sich nur noch um die Darstellung.
     * <p>
     * Keine Logik in der View!
     */

    private void renderBookPage(HttpServletResponse response, Book book)

            throws IOException {

        response.setContentType(CONTENT_TYPE_HTML);

        try (PrintWriter out = response.getWriter()) {

            out.println("<!doctype html>");

            out.println("<html lang=\"de\">");

            out.println("<head>");

            out.println("  <meta charset=\"UTF-8\" />");

            out.println("  <title>" + escapeHtml(book.getTitle()) + "</title>");

            out.println("  <style>");

            out.println("    body { font-family: Arial, sans-serif; max-width: 600px; margin: 50px auto; }");

            out.println("    .book { background: #f8f9fa; padding: 20px; border-radius: 8px; }");

            out.println("    .book h2 { margin-top: 0; color: #007bff; }");

            out.println("    .book ul { list-style: none; padding: 0; }");

            out.println("    .book li { padding: 5px 0; }");

            out.println("    .book strong { display: inline-block; width: 120px; }");

            out.println("    a { color: #007bff; text-decoration: none; }");

            out.println("  </style>");

            out.println("</head>");

            out.println("<body>");

            out.println("  <h1>Buch gefunden</h1>");

            out.println("  <div class=\"book\">");

            out.println("    <h2>" + escapeHtml(book.getTitle()) + "</h2>");

            out.println("    <ul>");

            out.println("      <li><strong>ISBN:</strong> " + escapeHtml(book.getIsbn()) + "</li>");

            out.println("      <li><strong>Autor:</strong> " + escapeHtml(book.getAuthor()) + "</li>");

            out.println("      <li><strong>Preis:</strong> CHF " + book.getPrice() + "</li>");

            out.println("      <li><strong>Seiten:</strong> " + book.getPages() + "</li>");

            if (book.getDate() != null && !book.getDate().isBlank()) {

                out.println("      <li><strong>Erschienen:</strong> " + escapeHtml(book.getDate()) + "</li>");

            }

            if (book.getSubtitle() != null && !book.getSubtitle().isBlank()) {

                out.println("      <li><strong>Untertitel:</strong> " + escapeHtml(book.getSubtitle()) + "</li>");

            }

            out.println("    </ul>");

            out.println("  </div>");

            out.println("  <p><a href=\"mvc-template\">← Zurück zur Suche</a></p>");

            out.println("</body>");

            out.println("</html>");

        }

    }

    /**
     * TODO: Hilfsmethode für Formularwerte.
     * <p>
     * Verhindert NullPointerException bei null-Werten.
     */

    private String valueOrEmpty(String value) {

        return value == null ? "" : value;

    }

    /**
     * TODO: HTML Escaping ist PFLICHT für User-Input!
     * <p>
     * <p>
     * <p>
     * Warum? Cross-Site Scripting (XSS) Angriffe verhindern.
     * <p>
     * Beispiel: User gibt ein: <script>alert('XSS')</script>
     * <p>
     * Ohne Escaping wird das Script im Browser ausgeführt!
     * <p>
     * Mit Escaping wird: &lt;script&gt;alert('XSS')&lt;/script&gt;
     * <p>
     * <p>
     * <p>
     * In echten Projekten: JSTL <c:out>, Thymeleaf th:text, etc.
     */

    private String escapeHtml(String value) {

        return valueOrEmpty(value)

                .replace("&", "&amp;")   // & zuerst!

                .replace("<", "&lt;")

                .replace(">", "&gt;")

                .replace("\"", "&quot;")

                .replace("'", "&#39;");

    }

    /**
     * TODO: Vereinfachtes URL Encoding für diese Vorlage.
     * <p>
     * <p>
     * <p>
     * Für produktiven Code besser:
     * <p>
     * URLEncoder.encode(value, StandardCharsets.UTF_8)
     * <p>
     * <p>
     * <p>
     * Hier bleibt es absichtlich minimal und gut lesbar.
     */

    private String encodeSimple(String value) {

        return value.trim().replace(" ", "%20");

    }

}