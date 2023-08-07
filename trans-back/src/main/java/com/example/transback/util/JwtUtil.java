package com.example.transback.util;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Base64;
import java.util.Date;

public class JwtUtil {
//    public static final String SECRET_KEY = generateSecretKey(); // 시크릿 키 설정
    public static final String SECRET_KEY ="YOUR_TEST_SECRET_KEY";
    public static final long EXPIRATION_TIME = 86400000; // JWT 만료 시간 설정 (24시간)

    public static String generateJWT(String email) {
        LocalDateTime currentTime = LocalDateTime.now();
        LocalDateTime expirationTime = currentTime.plusSeconds(EXPIRATION_TIME);

        // JWT 생성
        String jwt = Jwts.builder()
                .setSubject("user") // 토큰 주제 설정 (사용자)
                .claim("email", email) // 이메일 정보 추가
                .setIssuedAt(Date.from(currentTime.atZone(ZoneId.systemDefault()).toInstant())) // 토큰 발급 시간 설정
                .setExpiration(Date.from(expirationTime.atZone(ZoneId.systemDefault()).toInstant())) // 토큰 만료 시간 설정
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY) // 시그니처 설정
                .compact();

        return jwt;
    }

    public static String extractEmailFromJWT(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();
            String email = claims.get("email", String.class);
            return email;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public static boolean validateJWT(String jwt) {
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(jwt)
                    .getBody();

            // 만료시간 검사
            Date expiration = claims.getExpiration();
            LocalDateTime expirationTime = expiration.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
            LocalDateTime currentTime = LocalDateTime.now();

            if (expirationTime.isBefore(currentTime)) {
                // 토큰이 만료되었을 경우
                return false;
            }
            return true; // 유효한 JWT인 경우 true 반환
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace(); // 예외 스택 트레이스 출력
            return false; // 유효하지 않은 JWT인 경우 false 반환
        }
    }

    private static String generateSecretKey() {
        byte[] bytes = new byte[64];
        SecureRandom secureRandom = new SecureRandom();
        secureRandom.nextBytes(bytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(bytes);
    }
}