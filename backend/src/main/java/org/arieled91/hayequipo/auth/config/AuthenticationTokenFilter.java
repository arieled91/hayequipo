package org.arieled91.hayequipo.auth.config;


import org.arieled91.hayequipo.auth.exception.AuthorizationException;
import org.arieled91.hayequipo.auth.model.User;
import org.arieled91.hayequipo.auth.service.AuthService;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.preauth.PreAuthenticatedAuthenticationToken;
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

    @Value("${authorization.header}")
    private String tokenHeader;


    @Override
    protected void doFilterInternal(@NotNull HttpServletRequest request, @NotNull HttpServletResponse response, @NotNull FilterChain chain) throws ServletException, IOException {

        final String sessionUuid = request.getHeader(tokenHeader);

        if (SecurityContextHolder.getContext().getAuthentication() == null && sessionUuid!=null && !sessionUuid.isEmpty()) {
            final User user = authService.findUserBySessionId(sessionUuid).orElseThrow(AuthorizationException::new);
            authenticate(user, request);
        }

        chain.doFilter(request, response);
    }

    private void authenticate(@NotNull User user, @NotNull HttpServletRequest request){
        final UserDetails userDetails = this.userDetailsService.loadUserByUsername(user.getUsername());
        final PreAuthenticatedAuthenticationToken authentication = new PreAuthenticatedAuthenticationToken(userDetails, null, userDetails.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
        logger.info("authenticated user " + user.getUsername() + ", setting security context");
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }
}