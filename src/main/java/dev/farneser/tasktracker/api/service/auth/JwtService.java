package dev.farneser.tasktracker.api.service.auth;

import dev.farneser.tasktracker.api.exceptions.InvalidTokenException;
import dev.farneser.tasktracker.api.exceptions.TokenExpiredException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
@Service
public class JwtService {

    // 120000 equals two minutes of token lifetime
    @Value("${jwt.expiration.access:120000}")
    private Long accessTokenExpiration;

    // 1209600000 equals two weeks of token lifetime
    @Value("${jwt.expiration.refresh:1209600000}")
    private Long refreshTokenExpiration;

    @Value("${jwt.secret}")
    private String secretKey;

    private static final String REFRESH_TOKEN_HEADER = "is_refresh_token";

    /**
     * Generates a JWT for the provided UserDetails.
     *
     * @param email The user email address.
     * @return A JWT as a String.
     */
    public String generateAccessToken(String email) {
        return this.generateToken(new HashMap<>(), new HashMap<>(), email, accessTokenExpiration);
    }

    /**
     * Generates a Refresh token for the provided UserDetails.
     *
     * @param email The user email address.
     * @return A Refresh token as a String.
     */
    public String generateRefreshToken(String email) {
        var headers = new HashMap<String, Object>();

        headers.put(REFRESH_TOKEN_HEADER, true);

        return this.generateToken(new HashMap<>(), headers, email, refreshTokenExpiration);
    }

    /**
     * Generates a JWT with additional custom claims for the provided UserDetails.
     *
     * @param claims Additional custom claims to include in the JWT.
     * @param email  The user email address.
     * @return A JWT as a String with specified custom claims.
     */
    public String generateToken(Map<String, Object> claims, Map<String, Object> headers, String email, Long expiration) {
        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(email)
                .setHeader(headers)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSignInKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    /**
     * Checks if a JWT is valid for the given UserDetails.
     *
     * @param token The JWT to be validated.
     * @param email The user email address.
     * @return True if the token is valid for the provided user, false otherwise.
     */
    public boolean isTokenValid(String token, String email) throws TokenExpiredException, InvalidTokenException {
        // this works because the refresh token is made in the form of a regular jwt token,
        // but with an additional header, and here you can check for the presence of this header
        return isRefreshTokenValid(token, email) && !isTokenRefresh(token);
    }

    /**
     * Checks if a refresh token is valid for the given UserDetails.
     *
     * @param token The refresh token to be validated.
     * @param email The user email address.
     * @return True if the token is valid for the provided user, false otherwise.
     */
    public boolean isRefreshTokenValid(String token, String email) throws TokenExpiredException, InvalidTokenException {
        var username = extractUsername(token);
        return username.equals(email) && !isTokenExpired(token);
    }

    /**
     * Checks if a JWT is a refresh token.
     *
     * @param token The JWT to be checked.
     * @return True if the token is a refresh token, false otherwise.
     */
    public boolean isTokenRefresh(String token) {
        var header = getHeader(token, REFRESH_TOKEN_HEADER, Boolean.class);

        return header != null && header;
    }

    /**
     * Checks if a JWT has expired.
     *
     * @param token The JWT to be checked for expiration.
     * @return True if the token has expired, false otherwise.
     */
    private boolean isTokenExpired(String token) throws TokenExpiredException, InvalidTokenException {
        return extractExpiration(token).before(new Date());
    }

    /**
     * Extracts the expiration date from a JWT.
     *
     * @param token The JWT from which to extract the expiration date.
     * @return The expiration date of the token.
     */
    private Date extractExpiration(String token) throws TokenExpiredException, InvalidTokenException {
        return extractClaim(token, Claims::getExpiration);
    }

    /**
     * Retrieves a specific header value from the JWT.
     *
     * @param token           The JWT from which to extract the header value.
     * @param headerKey       The key of the header whose value is to be retrieved.
     * @param headerValueType The Class representing the type of the expected header value.
     * @param <T>             The generic type of the expected header value.
     * @return The value of the specified header if present and of the expected type, otherwise null.
     */
    public <T> T getHeader(String token, String headerKey, Class<T> headerValueType) {
        var claimsJws = Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token);

        var header = claimsJws.getHeader().get(headerKey);

        if (headerValueType.isInstance(header)) {
            return headerValueType.cast(header);
        }

        return null;
    }

    /**
     * This function extracts the username from a given JWT
     *
     * @param token The JWT from which the username is to be extracted
     * @return The extracted username
     */
    public String extractUsername(String token) throws TokenExpiredException, InvalidTokenException {
        return extractClaim(token, Claims::getSubject);
    }

    /**
     * This generic function allows for the extraction of custom claims from a JWT using a specified
     * claims resolver
     *
     * @param token          The JWT from which the claim is to be extracted
     * @param claimsResolver A functional interface representing the logic to extract a specific claim from
     *                       JWT claims
     * @param <T>            The extracted claim as per the specified type, providing flexibility to extract
     *                       different types of claims from the JWT
     * @return All claims present in the JWT
     */
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) throws TokenExpiredException, InvalidTokenException {
        var claims = extractClaims(token);

        return claimsResolver.apply(claims);
    }

    /**
     * Method that extract claims form JWT
     *
     * @param token Defile jwt as String
     * @return All claims in a jwt
     */
    private Claims extractClaims(String token) throws TokenExpiredException, InvalidTokenException {
        try {
            return Jwts.parserBuilder().setSigningKey(getSignInKey()).build().parseClaimsJws(token).getBody();
        } catch (ExpiredJwtException e) {
            log.info(e.getMessage());

            throw new TokenExpiredException("Token expired");
        } catch (Exception e) {
            throw new InvalidTokenException("Token is invalid");
        }
    }

    /**
     * This private method retrieves the signing key used for JWT validation
     *
     * @return The signing key derived from the provided secret key
     */
    private Key getSignInKey() {
        var keyBytes = Decoders.BASE64.decode(secretKey);

        return Keys.hmacShaKeyFor(keyBytes);
    }
}
