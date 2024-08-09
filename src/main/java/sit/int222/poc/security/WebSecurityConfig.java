package sit.int222.poc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class WebSecurityConfig {

    // Array containing URL patterns that do not require authentication (whitelisted URLs)
    private static final String[] WHITE_LIST_URL = {
            "/poc/api/auth/**",
    };

    // Injecting the custom JWT authentication filter and the authentication provider
    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    /**
     * Configures the security filter chain for the application.
     * @param http The HttpSecurity object that allows configuring web-based security for specific HTTP requests.
     * @return A configured SecurityFilterChain that defines how security should be handled for the application.
     * @throws Exception If an error occurs while configuring the security filter chain.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // 1. Disable CSRF protection as it is not needed for JWT-based stateless authentication
                .csrf(AbstractHttpConfigurer::disable)

                // 2. Configure URL authorization rules
                .authorizeHttpRequests(req ->
                        req.requestMatchers(WHITE_LIST_URL) // Allow access to whitelisted URLs without authentication
                                .permitAll()
                                .anyRequest() // All other requests require authentication
                                .authenticated()
                )

                // 3. Set session management to stateless since JWT is used
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // 4. Use the custom authentication provider for handling authentication
                .authenticationProvider(authenticationProvider)

                // 5. Add the JWT authentication filter before the UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
        ;

        // Build and return the configured SecurityFilterChain
        return http.build();
    }
}
