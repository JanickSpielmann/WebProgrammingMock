package bookstore.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.Base64;

@WebFilter("/api/orders/*")
public class LoginFilter extends HttpFilter {

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse resp, FilterChain chain)
            throws IOException, ServletException {
        String method = req.getMethod();
        if (method.equals("GET") || method.equals("DELETE")) {


            String authHeader = req.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Basic ")) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setHeader("WWW-Authenticate", "Basic realm=\"bookstore\"");
                return;
            }

            String decoded = new String(Base64.getDecoder().decode(authHeader.substring(6)));
            String[] parts = decoded.split(":", 2);
            String username = parts[0];
            String password = parts[1];


            if (!username.equals("admin") || !password.equals("12345")) {
                resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                resp.setHeader("WWW-Authenticate", "Basic realm=\"bookstore\"");
                return;
            }

        }
        chain.doFilter(req, resp);
    }
}
