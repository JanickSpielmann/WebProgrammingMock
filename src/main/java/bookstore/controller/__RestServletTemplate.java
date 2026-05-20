package bookstore.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Executable REST servlet template for exam preparation.
 * ToDo: Copy this class, rename it, change the @WebServlet mapping,
 * replace the in-memory list with your real service/model logic.
 */

@WebServlet("/template/rest/*") // ToDo: Change this to your real API path, e.g. "/api/books/*".
public class __RestServletTemplate extends HttpServlet {

    /**ToDo: ObjectMapper converts Java objects to JSON and JSON to Java objects.*/
    private final ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();

    /**ToDo: Example in-memory data so the servlet runs without a database or service class.*/
    private final List<TemplateItem> items = new ArrayList<>(List.of(
            new TemplateItem("1", "First example item"),
            new TemplateItem("2", "Second example item")
    ));

    /**
     * ToDo: GET is used to read resources.
     *
     * Example URLs:
     * /template/rest              -> returns all items
     * /template/rest?query=first  -> returns filtered items
     * /template/rest/1            -> returns item with id 1
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {

        /* ToDo: pathInfo is the part after the servlet mapping.
        *Example: /template/rest/1 -> pathInfo is "/1".*/
        String pathInfo = request.getPathInfo();

        // ToDo: Always set the response type before writing JSON.
        response.setContentType("application/json");

        // ToDo: No extra path means collection endpoint.
        if (pathInfo == null) {
            String query = request.getParameter("query"); // ToDo: Optional query parameter.

            List<TemplateItem> result = query == null || query.isBlank()
                    ? items
                    : items.stream()
                            .filter(item -> item.name().toLowerCase().contains(query.toLowerCase()))
                            .toList();

            objectMapper.writeValue(response.getOutputStream(), result);
            return;
        }

        // ToDo: A trailing slash without id is not a valid resource in this template.
        if (pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        // ToDo: Remove the leading slash to get the id.
        String id = pathInfo.substring(1);

        TemplateItem item = findById(id);
        if (item == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        objectMapper.writeValue(response.getOutputStream(), item);
    }

    /**
     * ToDo: POST is used to create a new resource.
     *
     * Example request body:
     * {"id":"3","name":"Third example item"}
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        // ToDo: POST to a collection endpoint is valid, POST to a specific id is rejected here.
        String pathInfo = request.getPathInfo();
        if (pathInfo != null && !pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }

        try {
            // ToDo: Read JSON request body and convert it into a Java object.
            TemplateItem item = objectMapper.readValue(request.getInputStream(), TemplateItem.class);

            if (item.id() == null || item.id().isBlank() || item.name() == null || item.name().isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            if (findById(item.id()) != null) {
                response.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }

            items.add(item);

            // ToDo: 201 Created is used when a new resource was created.
            response.setStatus(HttpServletResponse.SC_CREATED);
            response.setHeader("Location", request.getRequestURL() + "/" + item.id());
            response.setContentType("application/json");
            objectMapper.writeValue(response.getOutputStream(), item);
        } catch (Exception ex) {
            // ToDo: Invalid JSON or invalid request body.
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * ToDo: PUT is used to replace or create a resource with a known id.
     *
     * Example URL:
     * /template/rest/3
     */
    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String id = pathInfo.substring(1);

        try {
            TemplateItem item = objectMapper.readValue(request.getInputStream(), TemplateItem.class);

            if (item.name() == null || item.name().isBlank()) {
                response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }

            TemplateItem updatedItem = new TemplateItem(id, item.name());
            TemplateItem oldItem = findById(id);

            if (oldItem != null) {
                items.remove(oldItem);
                items.add(updatedItem);
                response.setStatus(HttpServletResponse.SC_OK);
            } else {
                items.add(updatedItem);
                response.setStatus(HttpServletResponse.SC_CREATED);
                response.setHeader("Location", request.getRequestURL().toString());
            }

            response.setContentType("application/json");
            objectMapper.writeValue(response.getOutputStream(), updatedItem);
        } catch (Exception ex) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }
    }

    /**
     * ToDo: DELETE is used to remove a resource.
     *
     * Example URL:
     * /template/rest/1
     */
    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) {
        String pathInfo = request.getPathInfo();
        if (pathInfo == null || pathInfo.equals("/")) {
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        String id = pathInfo.substring(1);
        TemplateItem item = findById(id);

        if (item == null) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }

        items.remove(item);

        // ToDo: 204 No Content is common when deletion succeeded and no body is returned.
        response.setStatus(HttpServletResponse.SC_NO_CONTENT);
    }

    // ToDo: Replace this helper method with a call to your service class.
    private TemplateItem findById(String id) {
        return items.stream()
                .filter(item -> item.id().equals(id))
                .findFirst()
                .orElse(null);
    }

    // ToDo: Replace this record with your real model class, e.g. Book, Product, Order.
    public record TemplateItem(String id, String name) {
    }
}
