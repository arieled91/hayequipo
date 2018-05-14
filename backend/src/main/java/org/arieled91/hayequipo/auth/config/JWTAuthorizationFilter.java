package org.arieled91.hayequipo.auth.config;


import io.jsonwebtoken.Jwts;
import org.arieled91.hayequipo.EnvProperties;
import org.jetbrains.annotations.Nullable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;

import static org.arieled91.hayequipo.auth.SecurityConstants.*;


public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private final EnvProperties envProperties;

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager, EnvProperties envProperties) {
        super(authenticationManager);
        this.envProperties = envProperties;
    }


    @Override
    protected void doFilterInternal(HttpServletRequest req,
                                    HttpServletResponse res,
                                    FilterChain chain) throws IOException, ServletException {
        final String header = req.getHeader(AUTH_HEADER);

        if (header == null || !header.startsWith(TOKEN_PREFIX)) {
            chain.doFilter(req, res);
            return;
        }

        final UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(req, res);
    }

    private @Nullable UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
        final String token = request.getHeader(AUTH_HEADER);
        if (token != null) {
            // parse the token.
            final String user = Jwts.parser()
                    .setSigningKey(envProperties.getAuthSecret().getBytes())
                    .parseClaimsJws(token.replace(TOKEN_PREFIX, ""))
                    .getBody()
                    .getSubject();

            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
            }
            return null;
        }
        return null;
    }
}