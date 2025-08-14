package hicc.budget.demo.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/css/**", "/images/**", "/js/**", "/h2-console/**").permitAll()
                        .requestMatchers("/api/auth/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/missions").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/v1/missions/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/missions/*/apply").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/expenses").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/v1/expenses/**").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/v1/expenses/*/apply").permitAll()

                        .requestMatchers(HttpMethod.POST, "/api/friends/request").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/friends/accept").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/friends/reject").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/friends").permitAll()
                        .requestMatchers(HttpMethod.GET,  "/api/friends/requests").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/api/friends/*").permitAll()

                        .requestMatchers(org.springframework.http.HttpMethod.POST, "/api/users").permitAll() // 회원가입
                        .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/users/**").permitAll() // 🔹 유저 조회 허용
                        .requestMatchers(HttpMethod.GET, "/api/friends/search").permitAll()
                        .anyRequest().authenticated()
                );

        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setExposedHeaders(Arrays.asList("Authorization"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}
