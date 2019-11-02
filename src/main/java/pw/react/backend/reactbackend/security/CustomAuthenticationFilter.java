package pw.react.backend.reactbackend.security;

import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class CustomAuthenticationFilter extends GenericFilterBean
{
    private String authorizationCode = "secretCode";

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException
    {
        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

        if(httpServletRequest.getHeader("Authorization") == null)
        {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication.");
            setUnauthorizedResponse(httpServletResponse);
            return;
        }

        if(!(httpServletRequest.getHeader("Authorization").equals(authorizationCode)))
        {
            httpServletResponse.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Invalid authentication.");
            setUnauthorizedResponse(httpServletResponse);
            return;
        }
        
        filterChain.doFilter(request, response);
    }

    private void setUnauthorizedResponse(HttpServletResponse response) {
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        response.setContentType("application/json");
        /*Response unAuthorizedResponse = Response.unauthorized().build();
        try {
            PrintWriter out = response.getWriter();
            out.println(unAuthorizedResponse.toJsonString());
        } catch (IOException e) {
            log.error("Error", e);
        }*/
    }
}
