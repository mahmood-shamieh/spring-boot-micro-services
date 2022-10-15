package com.javainuse.config;

import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {
    @Autowired
    private Base64.Encoder encoder;
    @Autowired
    private Base64.Decoder decoder;

    private static final long serialVersionUID = -2550185165626007488L;

    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    @Value("${jwt.secret}")
    private String secret;

    public String getUsernameFromToken(String token) {
        return getUsernameClaimFromToken(token);

    }
    public String getPasswordFromToken(String token) {
        return getPasswordClaimFromToken(token);

    }

    public Date getIssuedAtDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getIssuedAt);
    }

    public Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }
    public String getUsernameClaimFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);

        return new String(decoder.decode( claims.get(encoder.encodeToString("username".getBytes(StandardCharsets.UTF_8))).toString()));
    }
    public String getPasswordClaimFromToken(String token) {
        final Claims claims = getAllClaimsFromToken(token);
        return new String(decoder.decode( claims.get(encoder.encodeToString("password".getBytes(StandardCharsets.UTF_8))).toString()));
    }

    private Claims getAllClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }

    private Boolean ignoreTokenExpiration(String token) {
        // here you specify tokens, for that the expiration is ignored
        return false;
    }

    public String generateToken(UserDetails userDetails) {
        Map<String, Object> claims = new HashMap<>();
        return doGenerateToken(claims, userDetails.getUsername(),userDetails.getPassword());
    }

    private String doGenerateToken(Map<String, Object> claims, String username,String password) {
        claims.put(encoder.encodeToString("username".getBytes(StandardCharsets.UTF_8)), encoder.encodeToString(username.getBytes(StandardCharsets.UTF_8)));
        claims.put(encoder.encodeToString("password".getBytes(StandardCharsets.UTF_8)), encoder.encodeToString(password.getBytes(StandardCharsets.UTF_8)));
        return Jwts.builder().setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000000))
                .signWith(SignatureAlgorithm.HS512, secret).compact();
    }

    public Boolean canTokenBeRefreshed(String token) {
        return (!isTokenExpired(token) || ignoreTokenExpiration(token));
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        final String password = getPasswordFromToken(token);
        return (username.equals(userDetails.getUsername()) && password.equals(userDetails.getPassword()) && !isTokenExpired(token));
    }

    public Boolean validateTokenWithNoUserServices(String token) {
        String username = getUsernameFromToken(token);
        String password = getPasswordClaimFromToken(token);
        return !isTokenExpired(token);
    }
}
