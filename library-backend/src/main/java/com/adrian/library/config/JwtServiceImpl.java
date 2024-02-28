package com.adrian.library.config;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtServiceImpl implements JwtService {

    private static final String SECRET_KEY = " vgl/bonZwaDIfD6VfhefhsVxuxGsro1LekyYrxnlZULb0UzHepUMCFZwB3MiIiaZEq+X3U9irnHxY4Si5MWhu6ufWTGIB5jlm1krrSouNW5Cydx4sWlfHj4E+3FIoIO9Syp4gRyZb2tMaWfZWMmMNcGNWqAa6CAQ3lY69F0SHMUESG+z/ZTyyByOT0h4tuLKfW3xex6AWyZwc+7hVzMPiIR814AATFeco22Cjmx+W63mbtuOQHaj3YZFhvhliBw1KcsoLhXgPpCHb5bdMBwJyvE6XPx93b3PX1q6wEcophWCZxMyN8GZhTuhTAApKTDdvT0jQpx2GLU2nFpnCIDViTdrcKhmIExY8IiLemIcavs= ";

    @Override
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    @Override
    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    @Override
    public String generateToken(UserDetails userDetails, boolean resetPassword) {
        return generateToken(new HashMap<>(), userDetails, resetPassword);
    }

    @Override
    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails, boolean resetPassword) {
        // 24 hours
        int timeToExpiry = 1000 * 60 * 60 * 24;
        // 15 minutes
        if (resetPassword) {
            timeToExpiry = 1000 * 60 * 15;
        }
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                // token will expire in 24 hours
                .setExpiration(new Date(System.currentTimeMillis() + timeToExpiry))
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    @Override
    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    @Override
    public boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    @Override
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    @Override
    public Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    @Override
    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
