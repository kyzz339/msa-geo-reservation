package com.jaehyun.demo.jwt;

import com.jaehyun.demo.core.enums.Role;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Collections;
import java.util.Date;

@Slf4j
@Component
public class JwtTokenProvider {
    //-Access/Refresh 토큰 생성 및 검증 로직
    private final Key key;
    private final long accessTokenValidity;
    private final long refreshTokenValidity;

    public JwtTokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.access-token-validity}") long accessTokenValidity,
            @Value("${jwt.refresh-token-validity}") long refreshTokenValidity
    ){
                this.key = Keys.hmacShaKeyFor(secret.getBytes());
                this.accessTokenValidity = accessTokenValidity;
                this.refreshTokenValidity = refreshTokenValidity;
    }

    public String generateAccessToken(String email , Role role){
        return createToken(email,role,accessTokenValidity);
    }

    public String generateRefreshToken(String email , Role role){
        return createToken(email, role , refreshTokenValidity);
    }

    public String createToken(String email , Role role , long validityMs){
        Date now = new Date();
        Date expiry = new Date(now.getTime() + validityMs);

        return Jwts.builder()
                .setSubject(email)
                .claim("role" , role.name())
                .setIssuedAt(now)
                .setExpiration(expiry)
                .signWith(key , SignatureAlgorithm.HS256)
                .compact();
    }

    public String getEmailFromToken(String token){
        return parseClaims(token).getBody().getSubject();
    }

    public Role getRoleFromToken(String token){
        String roleName = parseClaims(token).getBody().get("role",String.class);
        return Role.valueOf(roleName);
    }

    public boolean validateToken(String token){
        try{
            parseClaims(token);
            return true;
        }catch (ExpiredJwtException e){
            log.warn("만료");
        }catch (JwtException | IllegalArgumentException e){
            log.warn("잘못된 JWT");
        }
        return false;
    }

    public Authentication getAuthentication(String token){
        String email = getEmailFromToken(token);
        Role role = getRoleFromToken(token);

        User userDetails = new User(
                email,
                "",
                Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name()))
        );

        return new UsernamePasswordAuthenticationToken(userDetails, null, Collections.singleton(new SimpleGrantedAuthority("ROLE_" + role.name())));
    }

    private Jws<Claims> parseClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token);
    }

}
