package org.example.model;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;
import java.util.Enumeration;

@Component
public class RequestResponseLoggingInterceptor implements HandlerInterceptor {

    private final Logger logger = LoggerFactory.getLogger(RequestResponseLoggingInterceptor.class);

//    @Override
//    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
//        logger.info("[preHandle][" + request + "]" + "[" + request.getMethod()
//                + "]" + request.getRequestURI() + getParameters(request));
//        return true;
//    }
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        // Log timestamp, IP address, endpoint, request method, headers, and parameters
        logger.info("[preHandle][" + request + "]" + "[" + request.getMethod()
                + "]" + request.getRequestURI() + getParameters(request),
                Instant.now(), // Timestamp
                request.getRemoteAddr(), // IP address
                request.getMethod(), // Request method
                request.getRequestURI(), // Endpoint
                getHeadersAndParameters(request)); // Headers and parameters
        return true;
    }

    private String getHeadersAndParameters(HttpServletRequest request) {
        // Include request headers and parameters
        // You can customize this method based on your requirements
        // For brevity, this example includes only headers
        StringBuilder headers = new StringBuilder();
        Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headers.append(headerName).append(": ").append(request.getHeader(headerName)).append(", ");
        }
        return headers.toString();
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
                           ModelAndView modelAndView) {
        logger.info("[postHandle][" + request + "]", response.getStatus(), getResponseBody(response));
    }

    private String getResponseBody(HttpServletResponse response) {
        // Extract response body if needed
        // You can customize this method based on your requirements
        // For brevity, this example just returns an empty string
        return "";
    }
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler,
                                Exception ex) {
        if (ex != null) {
            ex.printStackTrace();
        }
        logger.info("[afterCompletion][" + request + "][exception: " + ex + "]");
    }


    private String getParameters(HttpServletRequest request) {
        StringBuilder posted = new StringBuilder();
        Enumeration<?> e = request.getParameterNames();
        if (e != null) {
            posted.append("?");
        }
        while (e.hasMoreElements()) {
            if (posted.length() > 1) {
                posted.append("&");
            }
            String curr = (String) e.nextElement();
            posted.append(curr).append("=");
            if (curr.contains("password")
                    || curr.contains("answer")
                    || curr.contains("pwd")) {
                posted.append("*****");
            } else {
                posted.append(request.getParameter(curr));
            }
        }
        String ip = request.getHeader("X-FORWARDED-FOR");
        String ipAddr = (ip == null) ? getRemoteAddr(request) : ip;
        if (!StringUtils.isEmpty(ipAddr)) {
            posted.append("&_psip=" + ipAddr);
        }
        return posted.toString();
    }

    private String getRemoteAddr(HttpServletRequest request) {
        String ipFromHeader = request.getHeader("X-FORWARDED-FOR");
        if (ipFromHeader != null && ipFromHeader.length() > 0) {
            return ipFromHeader;
        }
        return request.getRemoteAddr();
    }
}