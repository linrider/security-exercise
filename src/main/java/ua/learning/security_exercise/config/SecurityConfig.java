package ua.learning.security_exercise.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import ua.learning.security_exercise.service.CustomOAuth2CustomerService;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {
        private final CustomOAuth2CustomerService customOAuth2CustomerService;
        @Value("${remember.me.key}")
        private String key;

        @Bean
        @SneakyThrows
        public SecurityFilterChain securityFilterChain(HttpSecurity http) {
                http
                                .csrf(AbstractHttpConfigurer::disable)
                                .authorizeHttpRequests(authorizeHttpRequest -> authorizeHttpRequest
                                                .requestMatchers("/admin/**", "/product/**").hasAnyRole("ADMIN", "ROOTADMIN")
                                                .requestMatchers("/auth/**", "/error", "/activation/**", "/css/**").permitAll()
                                                .anyRequest().authenticated())
                                .formLogin(formLogin -> formLogin
                                                .loginPage("/auth/login")
                                                .loginProcessingUrl("/process_login")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/auth/login?error"))
                                .oauth2Login(oauth2Login -> oauth2Login
                                                .loginPage("/auth/login")
                                                .defaultSuccessUrl("/", true)
                                                .failureUrl("/auth/login?error")
                                                .userInfoEndpoint(userInfoEndpoint -> userInfoEndpoint
                                                                .userService(customOAuth2CustomerService)))
                                .logout(logout -> logout
                                                .logoutUrl("/auth/logout")
                                                .logoutSuccessUrl("/auth/login"))
                                .rememberMe(rememberMe -> rememberMe
                                                .key(key)
                                                .tokenValiditySeconds(7 * 24 * 60 * 60)
                                                .alwaysRemember(false)
                                                .rememberMeParameter("remember-me"))
                                .sessionManagement(sessionManagement -> sessionManagement
                                                .sessionFixation().none()
                                                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                                                .maximumSessions(4)
                                                .expiredSessionStrategy(event -> event
                                                                .getResponse().sendRedirect("/auth/login")));

                return http.build();
        }
}
