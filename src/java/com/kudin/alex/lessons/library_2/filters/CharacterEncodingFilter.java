
package com.kudin.alex.lessons.library_2.filters;

import java.io.IOException;
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;

/**
 * @author ALEKSANDR KUDIN
 * @since May 18, 2018
 */
public class CharacterEncodingFilter implements Filter {

    private FilterConfig filterConfig = null;
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        chain.doFilter(request, response);
    }

    // ...

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
    }


    @Override
    public void destroy() {
        this.filterConfig = null;
    }
}