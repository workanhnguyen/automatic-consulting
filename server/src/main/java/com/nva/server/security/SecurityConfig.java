package com.nva.server.security;

import com.nva.server.entities.Role;
import com.nva.server.services.UserService;
import com.nva.server.views.login.LoginView;
import com.vaadin.flow.spring.security.VaadinWebSecurity;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
@RequiredArgsConstructor
public class SecurityConfig extends VaadinWebSecurity {
    private final JwtAuthenticationFilter jwtAuthenticationFilter;
    private final UserService userService;

    @Bean
    @Order(1)
    public SecurityFilterChain apiSecurityFilterChain(HttpSecurity http) throws Exception {
        http
                .securityMatcher("/api/**")
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> {
                    request.requestMatchers("/api/v1/auth/**").permitAll();
                    request.requestMatchers("/api/v1/users/**").hasAnyAuthority(Role.ROLE_USER.name());
                    request.requestMatchers("/api/v1/admin/**").hasAnyAuthority(Role.ROLE_ADMIN.name());
                    request.requestMatchers("/api/v1/dialogflow/**").permitAll();
                    request.anyRequest().authenticated();
                })
                .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider())
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//                .exceptionHandling(exceptionHandling -> {
//                            exceptionHandling.accessDeniedHandler(new CustomExceptionHandler());
//                            exceptionHandling.
//                        }
//                );

        return http.build();
    }

    @Override
    @Order(2)
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeHttpRequests(auth -> {
                auth.requestMatchers(
                        AntPathRequestMatcher.antMatcher(HttpMethod.GET, "/images/**")).permitAll();
        });
        super.configure(http);
        setLoginView(http, LoginView.class);
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        // Customize your WebSecurity configuration.
        super.configure(web);
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userService.userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());

        return authenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return userService.userDetailsService();
    }
}
