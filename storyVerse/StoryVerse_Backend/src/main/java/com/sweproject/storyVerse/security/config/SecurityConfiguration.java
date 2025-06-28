package com.sweproject.storyVerse.security.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import static com.sweproject.storyVerse.security.user.Permission.*;
import static com.sweproject.storyVerse.security.user.Role.*;
import static org.springframework.http.HttpMethod.*;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
    private static final String[] WHITE_LIST_URL = {"/api/v1/account/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/v3/api-docs/**",
            "/swagger-resources",
            "/swagger-resources/**",
            "/configuration/ui",
            "/configuration/security",
            "/swagger-ui/**",
            "/webjars/**",
            "/swagger-ui.html"
    };
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL)
                                .permitAll()
                                // contributor URLs
                                .requestMatchers("/api/v1/contributors/**").hasAnyRole(ADMIN.name(), CONTRIBUTOR.name())
                                .requestMatchers(GET, "/api/v1/contributors/**").hasAnyAuthority(ADMIN_READ.name(), CONTRIBUTOR_READ.name())
                                .requestMatchers(POST, "/api/v1/contributors/**").hasAnyAuthority(ADMIN_CREATE.name(), CONTRIBUTOR_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/contributors/**").hasAnyAuthority(ADMIN_UPDATE.name(), CONTRIBUTOR_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/contributors/**").hasAnyAuthority(ADMIN_DELETE.name(), CONTRIBUTOR_DELETE.name())
                                // author URLs
//                                .requestMatchers("/api/v1/authors/**").hasAnyRole(ADMIN.name(), AUTHOR.name())
//                                .requestMatchers(GET, "/api/v1/authors/**").hasAnyAuthority(ADMIN_READ.name(), AUTHOR_READ.name())
//                                .requestMatchers(POST, "/api/v1/authors/**").hasAnyAuthority(ADMIN_CREATE.name(), AUTHOR_CREATE.name())
//                                .requestMatchers(PUT, "/api/v1/authors/**").hasAnyAuthority(ADMIN_UPDATE.name(), AUTHOR_UPDATE.name())
//                                .requestMatchers(DELETE, "/api/v1/authors/**").hasAnyAuthority(ADMIN_DELETE.name(), AUTHOR_DELETE.name())
                                // new one
                                .requestMatchers("/api/v1/writers/**").hasAnyRole(ADMIN.name(), AUTHOR.name())
                                .requestMatchers(GET, "/api/v1/writers/**").hasAnyAuthority(ADMIN_READ.name(), AUTHOR_READ.name())
                                .requestMatchers(POST, "/api/v1/writers/**").hasAnyAuthority(ADMIN_CREATE.name(), AUTHOR_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/writers/**").hasAnyAuthority(ADMIN_UPDATE.name(), AUTHOR_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/writers/**").hasAnyAuthority(ADMIN_DELETE.name(), AUTHOR_DELETE.name())
                                // admin URLs
                                .requestMatchers("/api/v1/admins/**").hasRole(ADMIN.name())
                                .requestMatchers(GET, "/api/v1/admins/**").hasAnyAuthority(ADMIN_READ.name())
                                .requestMatchers(POST, "/api/v1/admins/**").hasAnyAuthority(ADMIN_CREATE.name())
                                .requestMatchers(PUT, "/api/v1/admins/**").hasAnyAuthority(ADMIN_UPDATE.name())
                                .requestMatchers(DELETE, "/api/v1/admins/**").hasAnyAuthority(ADMIN_DELETE.name())
                                .anyRequest()
                                .authenticated()
                )
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .logout(logout ->
                        logout.logoutUrl("/api/v1/auth/logout")
                                .addLogoutHandler(logoutHandler)
                                .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext())
                );

        return http.build();
    }
}
