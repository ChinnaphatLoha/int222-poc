package sit.int222.poc.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import sit.int222.poc.user_account.User;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${application.security.jwt.secret-key}")
    private String secretKey;

    @Value("${application.security.jwt.expiration}")
    private long jwtExpiration;

    @Value("${public.url}")
    private String publicUrl;

    /**
     * Extracts the username (subject) from the given JWT token.
     *
     * @param token The JWT token from which to extract the username.
     * @return The username contained in the JWT token.
     */
    public String extractName(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * Extracts a specific claim from the JWT token using the provided claims' resolver.
     *
     * @param <T> The type of the claim to be returned.
     * @param token The JWT token from which to extract the claim.
     * @param claimsResolver A function that specifies how to extract the claim from the token's claims.
     * @return The claim extracted from the JWT token.
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    /**
     * Generates a JWT token for the given user without any additional claims.
     *
     * @param user The user details for which the JWT token will be generated.
     * @return A JWT token containing the username and expiration information.
     */
    public String generateToken(User user) {
        Map<String, Object> extraClaims = new HashMap<>();
        extraClaims.put("iss", publicUrl);
        extraClaims.put("name", user.getName());
        extraClaims.put("email", user.getEmail());
        extraClaims.put("role", user.getRole());
        return generateTokenWithClaims(extraClaims, user);
    }

    /**
     * Generates a JWT token for the given user, including additional claims.
     *
     * @param extraClaims A map of additional claims to be included in the JWT token.
     * @param user The user details for which the JWT token will be generated.
     * @return A JWT token containing the username, expiration information, and any additional claims.
     */
    public String generateTokenWithClaims(Map<String, Object> extraClaims, User user) {
        return buildToken(extraClaims, user, jwtExpiration);
    }

    /**
     * Constructs a JWT token with the specified claims, user details, and expiration time.
     *
     * @param extraClaims A map of additional claims to be included in the JWT token.
     * @param user The user details for which the JWT token will be generated.
     * @param expiration The expiration time in milliseconds for the JWT token.
     * @return A JWT token containing all specified information.
     */
    private String buildToken(Map<String, Object> extraClaims, User user, long expiration) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(user.getName())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Validates the JWT token by checking if the username matches the one in the UserDetails
     * and if the token is not expired.
     *
     * @param token The JWT token to be validated.
     * @param user The user details against which the token will be validated.
     * @return True if the token is valid, false otherwise.
     */
    public boolean isTokenValid(String token, User user) {
        final String name = extractName(token);
        return (name.equals(user.getName())) && !isTokenExpired(token);
    }

    /**
     * Extracts all claims from the given JWT token.
     *
     * @param token The JWT token from which to extract the claims.
     * @return The claims contained in the JWT token.
     */
    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * Checks if the JWT token has expired by comparing the expiration date to the current date.
     *
     * @param token The JWT token to be checked for expiration.
     * @return True if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from the JWT token.
     *
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date of the JWT token.
     */
    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Decodes the secret key and returns it as a Key object for signing JWT tokens.
     *
     * @return The Key object used to sign JWT tokens.
     */
    private Key getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}