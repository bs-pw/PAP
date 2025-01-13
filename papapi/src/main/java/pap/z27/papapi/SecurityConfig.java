package pap.z27.papapi;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import pap.z27.papapi.domain.subclasses.Password;

@Configuration
public class SecurityConfig {
    @Bean
    public PasswordEncoder passwordEncoder() {
        return PasswordEncoderFactories.createDelegatingPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf().disable() // Disable CSRF for simplicity; enable in production as needed
                .authorizeHttpRequests()
                    .anyRequest().permitAll() // Allow all requests
                .and()
                .httpBasic().disable() // Disable HTTP Basic authentication
                .formLogin().disable(); // Disable form-based login

        return http.build();
    }
}