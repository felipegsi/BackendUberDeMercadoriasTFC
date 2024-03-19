package com.project.uber.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {


     private final SecurityFilter securityFilter;

    @Autowired
    @Lazy
    public SecurityConfiguration( SecurityFilter securityFilter) {

        this.securityFilter = securityFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(authoriza -> authoriza
                        //.requestMatchers(HttpMethod.GET, "/usuarios/admin").hasRole("ADMIN")
                        //.requestMatchers(HttpMethod.GET, "/usuarios/user").hasRole("USER")
                        //so altera AQUI
                        .requestMatchers(HttpMethod.POST, "/client/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/client/login").permitAll()
                        .requestMatchers(HttpMethod.POST, "/client/orderHistory").permitAll()
                        .requestMatchers(HttpMethod.POST, "/client/deleteClient").permitAll()
                        .requestMatchers(HttpMethod.POST, "/client/sendSimpleMessage").permitAll()
                        .requestMatchers(HttpMethod.POST, "/driver/register").permitAll()
                        .requestMatchers(HttpMethod.POST, "/driver/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
               // .addFilterBefore(driverSecurityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
            throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

}
