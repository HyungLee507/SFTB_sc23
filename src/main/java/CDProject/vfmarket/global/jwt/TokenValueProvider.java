package CDProject.vfmarket.global.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class TokenValueProvider {
    @Value("${jwt.secretKey}")
    private String secretKey;


    public Long extractUserId(String token) {
        String trim = token.replace("Bearer ", "");
        Claims claims = extractClaims(trim);
        return Long.parseLong(claims.get("userId").toString());
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(secretKey.getBytes())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }
}
