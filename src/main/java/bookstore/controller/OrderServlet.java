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

    private static final String CONTENT_TYPE_HTML = "text/html;charset=UTF-8";
    private final ObjectMapper objectMapper = ObjectMapperFactory.createObjectMapper();


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            List<Order> orders = OrderService.getOrders();
            resp.setContentType(CONTENT_TYPE_HTML);
            objectMapper.writeValue(resp.getOutputStream(), orders);
            return;
        }
        resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
        String pathInfo = req.getPathInfo();
        if (pathInfo == null) {
            OrderService.addOrder(new Order());
        }
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp){
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

