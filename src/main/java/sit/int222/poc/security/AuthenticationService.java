package sit.int222.poc.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sit.int222.poc.security.dto.AuthenticationRequest;
import sit.int222.poc.security.dto.AuthenticationResponse;
import sit.int222.poc.security.dto.RegisterRequest;
import sit.int222.poc.user_account.User;
import sit.int222.poc.user_account.UserRepository;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    // Injecting necessary dependencies for user authentication and JWT handling
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder passwordEncoder;

    /**
     * Authenticates a user based on the provided authentication request.
     * @param request The authentication request containing the username and password.
     * @return An AuthenticationResponse containing the generated JWT token for the authenticated user.
     * @throws UsernameNotFoundException If the username provided in the request does not exist in the database.
     */
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        // 1. Authenticate the user credentials using the AuthenticationManager
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),  // The username extracted from the request
                        request.getPassword()   // The password extracted from the request
                )
        );

        // 2. Retrieve the user details from the database using the provided username
        var user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 3. Generate a JWT token for the authenticated user using the JwtService
        String token = jwtService.generateToken(user);

        // 4. Return an AuthenticationResponse containing the JWT token
        return AuthenticationResponse.builder()
                .accessToken(token) // Setting the generated JWT token in the response
                .build();
    }

    public AuthenticationResponse register(RegisterRequest request) {
        var user = User.builder()
                .username(request.getUsername())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();
        userRepository.save(user);
        return AuthenticationResponse.builder()
                .accessToken(jwtService.generateToken(user))
                .build();
    }
}
