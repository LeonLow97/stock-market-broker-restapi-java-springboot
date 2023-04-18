package com.stockmarket.stockmarketapi;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import com.stockmarket.stockmarketapi.filters.AuthFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import java.io.IOException;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class AuthFilterTest {

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpServletResponse response;

    @Mock
    private FilterChain chain;

    private AuthFilter authFilter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        authFilter = new AuthFilter();
    }

    // @Test
    // void testDoFilterWithValidToken() throws IOException, ServletException {
    // // given
    // String token = "valid-token";
    // String authHeader = "Bearer " + token;

    // when(request.getHeader("Authorization")).thenReturn(authHeader);
    // doNothing().when(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));

    // Claims claims = Jwts.claims();
    // claims.put("userId", 123);
    // String jwt = Jwts.builder()
    // .setClaims(claims)
    // .signWith(SignatureAlgorithm.HS256, Constants.API_SECRET_KEY)
    // .compact();

    // // when
    // authFilter.doFilter(request, response, chain);

    // // then
    // verify(request).setAttribute("userId", 123);
    // verify(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));
    // }


    @Test
    void testDoFilterWithInvalidToken() throws IOException, ServletException {
        // given
        String token = "invalid-token";
        String authHeader = "Bearer " + token;

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        doNothing().when(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));

        // when
        authFilter.doFilter(request, response, chain);

        // then
        verify(response).sendError(HttpStatus.FORBIDDEN.value(), "invalid/expired token");
    }

    @Test
    void testDoFilterWithMissingAuthorizationHeader() throws IOException, ServletException {
        // given
        when(request.getHeader("Authorization")).thenReturn(null);
        doNothing().when(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));

        // when
        authFilter.doFilter(request, response, chain);

        // then
        verify(response).sendError(HttpStatus.FORBIDDEN.value(), "Authorization token must be provided");
    }

    @Test
    void testDoFilterWithInvalidAuthorizationHeader() throws IOException, ServletException {
        // given
        String authHeader = "invalid-auth-header";

        when(request.getHeader("Authorization")).thenReturn(authHeader);
        doNothing().when(chain).doFilter(any(ServletRequest.class), any(ServletResponse.class));

        // when
        authFilter.doFilter(request, response, chain);

        // then
        verify(response).sendError(HttpStatus.FORBIDDEN.value(),
                "Authorization token must be Bearer [token]");
    }

}

