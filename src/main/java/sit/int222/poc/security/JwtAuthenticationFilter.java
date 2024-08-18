package sit.int222.poc.security;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import sit.int222.poc.user_account.User;
import sit.int222.poc.user_account.UserRepository;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    // Constants defining the expected structure of the Authorization header
    private static final String HEADER_NAME = "Authorization";
    private static final String BEARER_PREFIX = "Bearer ";
    // Injecting dependencies required for JWT operations and user details retrieval
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;

    /**
     * This method is invoked once per request to filter and process the incoming HTTP request.
     * It checks the Authorization header for a JWT token, validates it, and sets up the security context.
     *
     * @param request     The incoming HTTP request.
     * @param response    The HTTP response.
     * @param filterChain The filter chain to pass the request and response to the next filter.
     * @throws ServletException In case of an error in filtering.
     * @throws IOException      In case of an I/O error during filtering.
     */
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        // Extracting the Authorization header from the request
        final String authHeader = request.getHeader(HEADER_NAME);
        final String jwt;
        final String username;

        // Checking if the Authorization header is missing or doesn't start with "Bearer "
        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            // If the header is invalid, continue with the next filter in the chain without processing the token
            filterChain.doFilter(request, response);
            return;
        }

        // Extracting the JWT token from the Authorization header (removing the "Bearer " prefix)
        jwt = authHeader.substring(BEARER_PREFIX.length());

        // Extracting the name from the JWT token using the JwtService
        username = jwtService.extractUsername(jwt);

        // If a name is extracted and there is no current authentication context
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            // Load user details from the database using the username
            User userDetails = (User) userDetailsService.loadUserByUsername(username);

            // Validate the token with the loaded user details to ensure it's authentic and not expired
            if (jwtService.isTokenValid(jwt, userDetails)) {
                // Create an authentication token with the user details and authorities
                UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails,
                        null, // No credentials are passed since we're working with JWT
                        userDetails.getAuthorities()
                );

                // Attach request details to the authentication token with additional details about the web request,
                // such as the user’s IP address and session ID,
                // which can be crucial for security auditing, logging, and managing user sessions.
                authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                // Set the authentication token in the security context, marking the user as authenticated
                SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        // Pass the request and response to the next filter in the chain, continuing the processing
        filterChain.doFilter(request, response);
    }
}
