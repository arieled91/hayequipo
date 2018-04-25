package org.arieled91.hayequipo.auth.config;

import org.arieled91.hayequipo.auth.model.VerificationToken;
import org.arieled91.hayequipo.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthService authService;
    private final UnauthorizedHandler unauthorizedHandler;

    @Value("${frontend.url}")
    private String frontendUrl;

    @Autowired
    public SecurityConfig(AuthService authService, UnauthorizedHandler unauthorizedHandler) {
        this.authService = authService;
        this.unauthorizedHandler = unauthorizedHandler;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and()
            .csrf().disable()
            .headers().frameOptions().disable().and()
            .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
            .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .oauth2Login()
                    .successHandler(authSuccessHandler())
                    .failureHandler(authFailureHandler()).and()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

            // disable page caching
            .headers().frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl();
        ;

    }
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler() {
        return (request, response, authentication) -> {
            final VerificationToken verification = authService.authenticate(authentication);
            response.sendRedirect(frontendUrl+"/#/login?token="+verification.getToken());
        };
    }

    @Bean
    public AuthenticationFailureHandler authFailureHandler() {
        return (request, response, authException) -> {
            response.sendRedirect(frontendUrl+"/#/login?message="+authException.getLocalizedMessage());
        };
    }

    @Bean
    public AuthenticationTokenFilter authenticationTokenFilterBean(){
        return new AuthenticationTokenFilter();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource()
    {
        final CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Arrays.asList("http://localhost:4200", "*"));
        config.setAllowedMethods(Arrays.asList("GET", "POST", "DELETE", "PUT", "PATCH", "HEAD", "OPTIONS","*"));
        config.setAllowedHeaders(Arrays.asList("Origin", "Accept", "X-Requested-With", "Content-Type", "Access-Control-Request-Method", "Access-Control-Request-Headers", "Authorization","*"));
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        return source;
    }
}
