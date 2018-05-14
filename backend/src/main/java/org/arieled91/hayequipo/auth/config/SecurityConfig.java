package org.arieled91.hayequipo.auth.config;

import org.arieled91.hayequipo.EnvProperties;
import org.arieled91.hayequipo.auth.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final AuthService authService;
    private final UnauthorizedHandler unauthorizedHandler;
    private final EnvProperties envProperties;
    private final UserDetailsService userDetailsService;

    @Autowired
    public SecurityConfig(AuthService authService, UnauthorizedHandler unauthorizedHandler, EnvProperties envProperties, UserDetailsService userDetailsService) {
        this.authService = authService;
        this.unauthorizedHandler = unauthorizedHandler;
        this.envProperties = envProperties;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.cors().configurationSource(corsConfigurationSource()).and()
            .csrf().disable()
            .headers().frameOptions().disable().and()
            .authorizeRequests()
                .antMatchers(
                        "/",
                        "/home",
                        "/login",
                        "/oauth2/authorization/*",
                        "/actuator",
                        "/auth/registration",
                        "/auth/registrationConfirm",
                        "/auth/login",
                        "/auth/users/current/privileges"
                ).permitAll()
                .anyRequest().authenticated().and()
                .addFilter(new JWTAuthenticationFilter(authenticationManager(), envProperties))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), envProperties))
                // don't create session
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .oauth2Login()
                    .successHandler(authSuccessHandler())
                    .failureHandler(authFailureHandler()).and()
            .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()

            // disable page caching
            .headers().frameOptions().sameOrigin()  // required to set for H2 else H2 Console will be blank.
                .cacheControl().and().and();


    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationSuccessHandler authSuccessHandler() {
        return (request, response, authentication) -> {
            final String token = authService.authenticateOauth2(authentication);
            response.sendRedirect(envProperties.getFrontendUrl()+"/#/login?token="+token);
        };
    }

    @Bean
    public AuthenticationFailureHandler authFailureHandler() {
        return (request, response, authException) -> {
            response.sendRedirect(envProperties.getFrontendUrl()+"/#/login?message="+authException.getLocalizedMessage());
        };
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
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

    @Override
    public void configure(AuthenticationManagerBuilder authBuilder) throws Exception {
        authBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }
}
