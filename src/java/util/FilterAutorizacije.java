package util;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebFilter(filterName = "AuthFilter", urlPatterns = {"*.xhtml"})
public class FilterAutorizacije implements Filter {

    public FilterAutorizacije() {
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response,
            FilterChain chain) throws IOException, ServletException {
        try {

            HttpServletRequest reqt = (HttpServletRequest) request;
            HttpServletResponse resp = (HttpServletResponse) response;
            HttpSession ses = reqt.getSession(false);

            String reqURI = reqt.getRequestURI();
            if (reqURI.contains("/index.xhtml") // pocetna stranica
                    || (ses != null ) // postoji ulogovan korisnik (postavljen username u sesiju)
                    || reqURI.contains("/public/") // javne strane
                    || reqURI.contains("javax.faces.resource")) { // za resurse (CSS, slike, itd.)
                chain.doFilter(request, response);
            } else {
                resp.sendRedirect(reqt.getContextPath() + "/faces/index.xhtml");
            }
        } catch (IOException | ServletException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void destroy() {

    }
}
