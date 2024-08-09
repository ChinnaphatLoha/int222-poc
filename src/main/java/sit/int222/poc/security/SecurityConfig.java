package sit.int222.poc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.argon2.Argon2PasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import sit.int222.poc.user_account.UserRepository;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

    // Dependency injection of UserRepository for accessing user data from the database.
    private final UserRepository userRepository;

    /**
     * Configures the UserDetailsService bean.
     * This service is responsible for loading user-specific data during authentication.
     * It retrieves the user details from the UserRepository using the provided username.
     *
     * @return A UserDetailsService implementation that fetches user data from the database.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    /**
     * Configures the AuthenticationProvider bean.
     * This provider processes authentication requests by retrieving user details and validating credentials.
     * It uses the configured UserDetailsService to load user data and the PasswordEncoder to verify passwords.
     *
     * @return An AuthenticationProvider that supports Dao-based authentication.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService()); // Sets the service to load user data.
        authProvider.setPasswordEncoder(passwordEncoder());       // Sets the encoder to validate passwords.
        return authProvider;
    }

    /**
     * Configures the AuthenticationManager bean.
     * The AuthenticationManager is the main entry point for authentication in Spring Security.
     * It manages the authentication process by coordinating with AuthenticationProviders.
     *
     * @param config The AuthenticationConfiguration which provides the default configuration.
     * @return The AuthenticationManager used to authenticate user credentials.
     * @throws Exception if an error occurs during configuration.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Configures the PasswordEncoder bean.
     * The PasswordEncoder is responsible for hashing and verifying passwords.
     * This implementation uses the Argon2 algorithm for secure password hashing.
     *
     * @return A PasswordEncoder that hashes passwords using the Argon2 algorithm.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        // These parameters might be configured in application.properties
        return new Argon2PasswordEncoder(9, 16, 1, 16, 2);
    }
}