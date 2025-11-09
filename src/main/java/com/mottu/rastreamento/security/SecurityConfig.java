package com.mottu.rastreamento.security;

import com.mottu.rastreamento.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
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
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**").disable())
                .cors(cors -> {})

                .authorizeHttpRequests(auth -> auth

                        .requestMatchers("/css/**", "/images/**", "/js/**", "/webjars/**").permitAll()
                        .requestMatchers("/login", "/logout").permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/api/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/api/**").hasRole("ADMIN")

                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/motos/**", "/sensores/**", "/alocacoes/**", "/manutencoes/**")
                        .hasAnyRole("USER","ADMIN")
                        .requestMatchers(HttpMethod.POST, "/motos/**", "/sensores/**", "/alocacoes/**", "/manutencoes/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PUT, "/motos/**", "/sensores/**", "/alocacoes/**", "/manutencoes/**")
                        .hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/motos/**", "/sensores/**", "/alocacoes/**", "/manutencoes/**")
                        .hasRole("ADMIN")

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login").permitAll()
                        .defaultSuccessUrl("/motos", true)
                )

                .httpBasic(httpBasic -> {})

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout").permitAll()
                );

        return http.build();
    }
}
