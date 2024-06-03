package org.ee.carrental.web.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebFilter({"/*"})
public class CharacterEncodingFilter extends HttpFilter {
    @Override
    protected void doFilter(HttpServletRequest req, HttpServletResponse res, FilterChain chain) throws IOException, ServletException {
        // ustawienie kodowania na UTF-8 jeśli nie zostało określone jawnie w żądaniu (domyślnie jest to ISO-8859-1) - używane przy odczycie danych wysyłanych POST'em
        if (req.getCharacterEncoding() == null)
            req.setCharacterEncoding("UTF-8");

        chain.doFilter(req,res);
    }
}
