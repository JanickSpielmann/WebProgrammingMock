package bookstore.controller;

import bookstore.model.Order;
import bookstore.model.OrderNotFoundException;
import bookstore.model.OrderService;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.List;

@WebServlet("/api/orders/*")
public class OrderServlet extends HttpServlet {

    private static final String CONTENT_TYPE_JSON = "application/json;charset=UTF-8";
    private final ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            List<Order> orders = OrderService.getOrders();
            resp.setContentType(CONTENT_TYPE_JSON);
            objectMapper.writeValue(resp.getOutputStream(), orders);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        resp.setContentType(CONTENT_TYPE_JSON);
        if (pathInfo == null) {
            Order order;
            try {
                order = objectMapper.readValue(req.getInputStream(), Order.class);
            } catch (IOException e) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            if (order == null || order.getIsbn() == null || order.getName() == null || order.getAddress() == null) {
                resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            OrderService.addOrder(order);
            resp.setStatus(HttpServletResponse.SC_CREATED);
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        if (pathInfo.equals("/")) {
            resp.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
            return;
        }
        try {
            OrderService.removeOrder(Integer.parseInt(pathInfo.substring(1)));
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (NumberFormatException ex) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        } catch (OrderNotFoundException ex) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

}

