package com.warehouse.warehouse_manager.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();

        http
                // 1. Настройка CSRF через Cookie (требование задания)
                .csrf(csrf -> csrf
                                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
                                .csrfTokenRequestHandler(requestHandler)
                        // Если ты тестируешь через Postman и возникают проблемы с 403 на POST,
                        // можно временно раскомментировать строку ниже:
                        // .ignoringRequestMatchers("/api/**")
                )

                // 2. Управление доступом
                .authorizeHttpRequests(auth -> auth
                        // ТЕПЕРЬ: регистрация доступна ТОЛЬКО пользователям с ролью ADMIN
                        .requestMatchers("/api/auth/register").hasRole("ADMIN")

                        // Просмотр данных (GET) разрешен и USER, и ADMIN
                        .requestMatchers(HttpMethod.GET, "/api/**").hasAnyRole("USER", "ADMIN")

                        // Все изменения (POST, PUT, DELETE) разрешены ТОЛЬКО ADMIN
                        // Сюда входит: добавление товара, списание брака, слияние складов
                        .requestMatchers("/api/**").hasRole("ADMIN")

                        // Все остальные запросы должны быть авторизованы
                        .anyRequest().authenticated()
                )

                // 3. Включаем Basic Auth (требование задания)
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }
}