package com.company.neuheathcaremanagement.config;

import jakarta.servlet.Filter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import java.util.Collections;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(request -> request
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/doctor/**").hasRole("DOCTOR")
                .requestMatchers("/patient/**").hasRole("PATIENT")
                .requestMatchers("/**").permitAll())
            .formLogin(form -> form
                .loginPage("/login")
                .defaultSuccessUrl("/userlogin", false)
                .failureUrl("/login"))
            .oauth2Login(oauth2 -> oauth2
                .loginPage("/login")
                .defaultSuccessUrl("/oauth2userlogin", true)
                .failureUrl("/login"))
            .logout(logout -> logout
                    .logoutUrl("/logout") 
                    .logoutSuccessUrl("/login?logout")
                    .permitAll())
            .csrf(csrf -> csrf.disable())
            .addFilterBefore(customSessionFilter(), UsernamePasswordAuthenticationFilter.class);
        
        return http.build();
    }

    @Bean
    public Filter customSessionFilter() {
        return (request, response, chain) -> {
            HttpServletRequest req = (HttpServletRequest) request;
            HttpSession session = req.getSession(false);

            if (session != null) {
                String userRole = (String) session.getAttribute("userRole");

                if (userRole != null) {
                    // Set authentication in Spring Security context
                    UsernamePasswordAuthenticationToken authentication =
                            new UsernamePasswordAuthenticationToken("user", null, 
                            Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + userRole.toUpperCase())));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            } 

            chain.doFilter(request, response);
        };
    }
}

