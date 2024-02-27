package com.nextuple.pranay.fullstack.security;

import com.nextuple.pranay.fullstack.TestUtil;
import com.nextuple.pranay.fullstack.model.Users;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;

import java.time.LocalDateTime;
import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class JWTAuthenticationFilterTest {

    @Mock
    private JWTTokenProvider jwtTokenProvider;

    @Mock
    private UserDetailsService userDetailsService;

    @InjectMocks
    private JWTAuthenticationFilter jwtAuthenticationFilter;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDoFilterInternal() throws Exception {
        HttpServletRequest request = mock(HttpServletRequest.class);
        HttpServletResponse response = mock(HttpServletResponse.class);
        FilterChain filterChain = mock(FilterChain.class);

        when(request.getHeader("Authorization")).thenReturn("Bearer token");
        when(jwtTokenProvider.validateToken(anyString())).thenReturn(true);
        when(jwtTokenProvider.getUsername(anyString())).thenReturn("username");
        when(userDetailsService.loadUserByUsername(anyString())).thenReturn(
                new org.springframework.security.core.userdetails.User(
                        "username",
                        "password",
                        new ArrayList<>()
                )
        );

        jwtAuthenticationFilter.doFilterInternal(request, response, filterChain);

        verify(request, times(1)).getHeader("Authorization");
        verify(jwtTokenProvider, times(1)).validateToken(anyString());
        verify(jwtTokenProvider, times(1)).getUsername(anyString());
        verify(userDetailsService, times(1)).loadUserByUsername(anyString());
        verify(filterChain, times(1)).doFilter(request, response);
    }
}
