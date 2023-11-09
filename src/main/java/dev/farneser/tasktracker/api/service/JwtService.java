package dev.farneser.tasktracker.api.service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    // 1000 * 3600 * 48 equals two days of token lifetime
    private static final long JWT_EXPIRATION_MS = 1000 * 3600 * 48;
    @Value("${jwt.secret}")
    private String secretKey;

    /**
     * Generates a JWT token for the provided UserDetails.
     *
     * @param userDetails The UserDetails object containing user information.
     * @return A JWT token as a String.
     */
    public String generateToken(UserDetails userDetails) {
        return this.generateToken(new HashMap<>(), userDetails);
    }

    /**
     * Generates a JWT token with additional custom claims for the provided UserDetails.
     *
     * @param extraClaims Additional custom claims to include in the JWT.
     * @param userDetails The UserDetails object containing user information.
     * @return A JWT token as a String with specified custom claims.
     */
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_EXPIRATION_MS))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if a JWT token is valid for the given UserDetails.
     *
     * @param token       The JWT token to be validated.
     * @param userDetails The UserDetails object for the user.
     * @return True if the token is valid for the provided user, false otherwise.
     */
    public boolean isTokenValid(String token, UserDetails userDetails) {
        var username = extractUsername(token);
        return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    /**
     * Checks if a JWT token has expired.
     *
     * @param token The JWT token to be checked for expiration.
     * @return True if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) {
        return extraExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT token.
     *
     * @param token The JWT token from which to extract the expiration date.
     * @return The expiration date of the token.
     */
    private Date extraExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * This function extracts the username from a given JWT token
     *
     * @param token The JWT token from which the username is to be extracted
     * @return The extracted username
     */
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * This generic function allows for the extraction of custom claims from a JWT token using a specified
     * claims resolver
     *
     * @param token          The JWT token from which the claim is to be extracted
     * @param claimsResolver A functional interface representing the logic to extract a specific claim from
     *                       JWT claims
     * @param <T>            The extracted claim as per the specified type, providing flexibility to extract
     *                       different types of claims from the JWT token
     * @return All claims present in the JWT token
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        var claims = extractClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Method that extract claims form JWT token
     *
     * @param token Defile jwt token as String
     * @return All claims in a jwt token
     */
    private Claims extractClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSignInKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    /**
     * This private method retrieves the signing key used for JWT token validation
     *
     * @return The signing key derived from the provided secret key
     */
    private Key getSignInKey() {
        var keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
