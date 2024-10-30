package ar.edu.um.prog.security;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.crypto.SecretKey;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import ar.edu.um.prog.exception.APIException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.MalformedJwtException;

import io.jsonwebtoken.UnsupportedJwtException;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.log4j.Log4j2;

@Component
@Log4j2
public class JwtTokenProvider {

    private static final String CLAIM_KEY_USERNAME = "sub";
    private static final String CLAIM_KEY_AUDIENCE = "audience";
    private static final String CLAIM_KEY_CREATED = "created";
    private static final String CLAIM_KEY_EXPIRATION = "exp";

    private static final String AUDIENCE_WEB = "web";


    @Value("${app.jwt.secret}")
    private String secretKey;
    
    @Value("${app.jwt.expiration}")
    private long jwtExpirationInMs;

    // generate token
    public String generateToken(Authentication authentication){
        Map<String, Object> claims = new HashMap<>();
        claims.put(CLAIM_KEY_USERNAME, authentication.getName());
        claims.put(CLAIM_KEY_AUDIENCE, AUDIENCE_WEB);
        //claims.put(CLAIM_KEY_AUDIENCE, generateAudience(device));
        claims.put(CLAIM_KEY_CREATED, new Date());
        claims.put(CLAIM_KEY_EXPIRATION, generateExpirationDate());

        var token = Jwts.builder().claims().empty().add(claims).and()
                .signWith(signWithKey())
                .compact();
    
      
        return token;
    }

    // get username from the token
    public String getUsernameFromJWT(String token){
        Claims claims = Jwts.parser()
        .verifyWith(signWithKey())
        .build()
        .parseSignedClaims(token)
        .getPayload();

        return claims.getSubject();
    }

    // validate JWT token
    public boolean validateToken(String token) {
        if (!StringUtils.hasText(token)) {
            return false;
        } 
        try {
            Claims claims = Jwts.parser()
                .verifyWith(signWithKey())
                .build()
                .parseSignedClaims(token)
                .getPayload();

            return !claims.getExpiration().before(new Date());
        } catch (MalformedJwtException ex) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Invalid JWT token");
        } catch (ExpiredJwtException ex) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Expired JWT token");
        } catch (UnsupportedJwtException ex) {
            throw new APIException(HttpStatus.BAD_REQUEST, "Unsupported JWT token");
        } catch (IllegalArgumentException ex) {
            throw new APIException(HttpStatus.BAD_REQUEST, "JWT claims string is empty.");
        }
    }

    public String parseBearerToken(String bearerToken){
            if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")){
                return bearerToken.substring(7, bearerToken.length());
            }
            return "";
    }

    private SecretKey signWithKey() {
        log.info("secretKey: {}", secretKey);
        log.info("secretKey length: {}", Decoders.BASE64.decode(secretKey).length);
        SecretKey key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secretKey));
        return key;
    }
    
    
    private Date generateExpirationDate() {
        return new Date(System.currentTimeMillis() + jwtExpirationInMs * 1000);
    }
}
