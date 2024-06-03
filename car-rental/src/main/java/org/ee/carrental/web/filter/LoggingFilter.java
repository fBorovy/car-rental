package org.ee.carrental.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Logger;

@WebFilter({"/*"})
public class LoggingFilter extends HttpFilter {
    private final static Logger log = Logger.getLogger(LoggingFilter.class.getName());

    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        log.info(()->
            "Request type:"+req.getMethod()
            +", contextPath:"+req.getContextPath()
            +", servletPath:"+req.getServletPath()
            +", pathInfo:"+req.getPathInfo()
        );
        chain.doFilter(req, res);
    }
}
