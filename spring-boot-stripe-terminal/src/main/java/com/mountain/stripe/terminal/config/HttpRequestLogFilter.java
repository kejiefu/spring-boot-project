package com.mountain.stripe.terminal.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.util.ContentCachingRequestWrapper;
import org.springframework.web.util.ContentCachingResponseWrapper;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

/**
 * 全局HTTP请求日志过滤器
 * 打印请求路径、请求参数、请求体、响应状态和耗时
 *
 * @author kejiefu
 * @date 2026-03-31
 */
@Component
@Order(1)
public class HttpRequestLogFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(HttpRequestLogFilter.class);

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        long start = System.currentTimeMillis();

        ContentCachingRequestWrapper requestWrapper = new ContentCachingRequestWrapper(request);
        ContentCachingResponseWrapper responseWrapper = new ContentCachingResponseWrapper(response);

        try {
            filterChain.doFilter(requestWrapper, responseWrapper);
        } finally {
            long cost = System.currentTimeMillis() - start;
            logRequest(requestWrapper, responseWrapper, cost);
            responseWrapper.copyBodyToResponse();
        }
    }

    private void logRequest(ContentCachingRequestWrapper request,
                            ContentCachingResponseWrapper response,
                            long costMs) {
        String method = request.getMethod();
        String uri = request.getRequestURI();
        String queryString = request.getQueryString();
        Map<String, String[]> parameterMap = request.getParameterMap();
        String requestBody = getRequestBody(request);
        int status = response.getStatus();
        String responseBody = getResponseBody(response);

        StringBuilder sb = new StringBuilder(512);
        sb.append("HTTP Request => method=").append(method)
                .append(", uri=").append(uri);

        if (StringUtils.hasText(queryString)) {
            sb.append(", query=").append(queryString);
        }

        if (parameterMap != null && !parameterMap.isEmpty()) {
            sb.append(", params=").append(toJsonSafe(parameterMap));
        }

        if (StringUtils.hasText(requestBody)) {
            sb.append(", body=").append(requestBody);
        }

        sb.append(", status=").append(status)
                .append(", costMs=").append(costMs);

        if (StringUtils.hasText(responseBody)) {
            sb.append(", responseBody=").append(responseBody);
        }

        log.info(sb.toString());
    }

    private String getRequestBody(ContentCachingRequestWrapper request) {
        byte[] content = request.getContentAsByteArray();
        if (content == null || content.length == 0) {
            return "";
        }
        return new String(content, StandardCharsets.UTF_8).trim();
    }

    private String getResponseBody(ContentCachingResponseWrapper response) {
        byte[] content = response.getContentAsByteArray();
        if (content == null || content.length == 0) {
            return "";
        }
        return new String(content, StandardCharsets.UTF_8).trim();
    }

    private String toJsonSafe(Map<String, String[]> parameterMap) {
        try {
            Map<String, Object> simpleMap = new HashMap<>(16);
            for (Map.Entry<String, String[]> entry : parameterMap.entrySet()) {
                String[] values = entry.getValue();
                if (values == null) {
                    simpleMap.put(entry.getKey(), null);
                } else if (values.length == 1) {
                    simpleMap.put(entry.getKey(), values[0]);
                } else {
                    simpleMap.put(entry.getKey(), values);
                }
            }
            return OBJECT_MAPPER.writeValueAsString(simpleMap);
        } catch (Exception ex) {
            return "{}";
        }
    }
}
