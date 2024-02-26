package com.sedefproject.webpage.security;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    private final AuthenticationProvider authenticationProvider;
    private final LogoutHandler logoutHandler;


    public SecurityConfig(JwtAuthFilter jwtAuthFilter, AuthenticationProvider authenticationProvider, LogoutHandler logoutHandler) {
        this.jwtAuthFilter = jwtAuthFilter;
        this.authenticationProvider = authenticationProvider;
        this.logoutHandler = logoutHandler;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(authorizeRequests ->
                        authorizeRequests.requestMatchers(
                                        "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
                                        "/posts/**","/categories/**","/subs/**"
                                        ,"/auth/login","/auth/register","/auth/refresh-token",
                                        "/user/forget-password","/auth/confirm","/user/deneme").permitAll()
                                .requestMatchers("/auth/user").authenticated()
                                .requestMatchers("/user/change-password","/user/reset-password").authenticated()
                                .requestMatchers("/comments/**").authenticated()
                                .requestMatchers("/auth/admin").hasRole("ADMIN")
                )
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class).logout(logout ->
                        logout
                                .logoutUrl("/auth/logout")
                                .addLogoutHandler(logoutHandler) //not: çıkış işlemi tamamlandığında yapılacak ekstra işlemleri belirtir.
                                .logoutSuccessHandler((request, response, authentication) -> {
                                    response.setStatus(HttpStatus.OK.value());
                                    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
                                    String jsonResponse = "{\"message\": \"Logout successful\"}";
                                    response.getWriter().write(jsonResponse);
                                })
                )
                .build();
    }

}
