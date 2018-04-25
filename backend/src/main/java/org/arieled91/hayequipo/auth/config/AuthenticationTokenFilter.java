package org.arieled91.hayequipo.auth.config;


import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.service.AuthService;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class AuthenticationTokenFilter extends OncePerRequestFilter {

    @Autowired
    private AuthService authService;

    @Autowired
    private UserDetailsService userDetailsService;

    private static final String AUTH_HEADER = "Authorization";
    private static final int BEARER_LENGTH = "Bearer ".length();


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {

        final String token = extractToken(request);

        if (SecurityContextHolder.getContext().getAuthentication() == null && token!=null && !token.isEmpty()) {
            authService.findUserByToken(token).ifPresent(user -> authorize(request, user));
        }

        chain.doFilter(request, response);
    }

    private void authorize(@NotNull HttpServletRequest request, @NotNull User user){
        final UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        final UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        logger.info("authenticated user " + user.getUsername() + ", setting security context");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private @Nullable String extractToken(HttpServletRequest request){
        final String rawToken = request.getHeader(AUTH_HEADER);
        return rawToken != null && rawToken.length() > BEARER_LENGTH ? rawToken.substring(BEARER_LENGTH) : null;
    }
}