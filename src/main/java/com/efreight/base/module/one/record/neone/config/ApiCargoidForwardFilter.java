package com.efreight.base.module.one.record.neone.config;

import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 兼容前端仍然请求 /api/cargoid/** 的本地直连场景。
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE)
public class ApiCargoidForwardFilter extends OncePerRequestFilter {

    private static final String API_PREFIX = "/api/cargoid";
    private static final String FORWARD_MARKER = ApiCargoidForwardFilter.class.getName() + ".FORWARDED";

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String requestUri = request.getRequestURI();
        Object forwarded = request.getAttribute(FORWARD_MARKER);
        if (forwarded == null && requestUri.startsWith(API_PREFIX)) {
            String forwardPath = requestUri.substring(API_PREFIX.length());
            if (forwardPath.isEmpty()) {
                forwardPath = "/";
            }
            request.setAttribute(FORWARD_MARKER, Boolean.TRUE);
            RequestDispatcher dispatcher = request.getRequestDispatcher(forwardPath);
            dispatcher.forward(request, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
