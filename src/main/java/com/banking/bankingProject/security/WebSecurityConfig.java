package com.banking.bankingProject.security;

import com.banking.bankingProject.entities.EndpointSecurity;
import com.banking.bankingProject.services.EndpointSecurityService;
import com.banking.bankingProject.services.JwtServiceImpl;
import com.banking.bankingProject.services.UserDetailsServiceImpl;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;
import java.util.List;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    private static final Logger LOGGER = LoggerFactory.getLogger(WebSecurityConfig.class);

    private final UserDetailsServiceImpl userDetailsServiceImpl;
    private final JwtServiceImpl jwtService;
    private final EndpointSecurityService endpointSecurityService;

    public WebSecurityConfig(UserDetailsServiceImpl userDetailsServiceImpl,
            JwtServiceImpl jwtService,
            EndpointSecurityService endpointSecurityService) {
        this.userDetailsServiceImpl = userDetailsServiceImpl;
        this.jwtService = jwtService;
        this.endpointSecurityService = endpointSecurityService;
    }

    /**
     * Context path is excluded from request patterns
     * Security rules are loaded from database
     *
     * @param http
     * @return
     * @throws Exception
     */
    @Bean
    public SecurityFilterChain config(HttpSecurity http) throws Exception {
        List<EndpointSecurity> securityRules = endpointSecurityService.getEnabledEndpointSecurityRules();
        LOGGER.info("Loading {} security rules from database", securityRules.size());

        return http.authorizeHttpRequests(registry -> {
            // Apply security rules from database
            for (EndpointSecurity rule : securityRules) {
                String[] roles = rule.getAllowedRoles().stream()
                        .map(roleEnum -> roleEnum.name().replace("ROLE_", ""))
                        .toArray(String[]::new);

                if (rule.getPermitAll()) {
                    if (rule.getHttpMethod() != null && !rule.getHttpMethod().isEmpty()) {
                        registry.requestMatchers(HttpMethod.valueOf(rule.getHttpMethod()), rule.getUrlPattern())
                                .permitAll();
                    } else {
                        registry.requestMatchers(rule.getUrlPattern()).permitAll();
                    }
                    LOGGER.debug("Permit all for: {} {}", rule.getHttpMethod(), rule.getUrlPattern());
                } else if (roles.length > 0) {
                    if (rule.getHttpMethod() != null && !rule.getHttpMethod().isEmpty()) {
                        registry.requestMatchers(HttpMethod.valueOf(rule.getHttpMethod()), rule.getUrlPattern())
                                .hasAnyRole(roles);
                    } else {
                        registry.requestMatchers(rule.getUrlPattern()).hasAnyRole(roles);
                    }
                    LOGGER.debug("Secured: {} {} - Roles: {}", rule.getHttpMethod(), rule.getUrlPattern(),
                            String.join(", ", roles));
                }
            }

            // Default: any other request must be authenticated
            registry.anyRequest().authenticated();
        })
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .addFilterBefore(new JwtAuthenticationFilter(jwtService, userDetailsServiceImpl),
                        UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(exception -> exception
                        .authenticationEntryPoint(authenticationEntryPoint()))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(Customizer.withDefaults())
                .httpBasic(Customizer.withDefaults())
                .userDetailsService(userDetailsServiceImpl)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    UrlBasedCorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(List.of("http://localhost:3000"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, exception) -> {
            LOGGER.error("Error: ", exception);
            response.setContentType("application/json");
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            String errorMessage = exception.getMessage() != null ? exception.getMessage() : "Authentication failed";
            response.getWriter().write(String.format(
                    "{\"error\":\"Unauthorized\",\"message\":\"%s\",\"status\":401,\"path\":\"%s\"}",
                    errorMessage, request.getRequestURI()));
        };
    }
}
