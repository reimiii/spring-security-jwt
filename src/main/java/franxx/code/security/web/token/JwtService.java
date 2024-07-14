package franxx.code.security.web.token;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Service
public class JwtService {
  private static final String SECRET_KEY = "7B2FDA14370B8251D762A22C881FD6F21446D9FC9AF8811755AEBF318081F081F743BDCD89BE5CFCBDB68F822589AB2B6C216638F50060844C7D9CE367FDB315";
  private static final Long VALID_TOKEN = TimeUnit.MINUTES.toMillis(30);

  public String generateToken(UserDetails userDetails) {
    Map<String, String> claims = new HashMap<>();
    claims.put("pacman", "arch-linux");

    return Jwts.builder()
        .claims(claims)
        .subject(userDetails.getUsername())
        .issuedAt(Date.from(Instant.now()))
        .expiration(Date.from(Instant.now().plusMillis(VALID_TOKEN)))
        .signWith(generateSecretKey())
        .compact();
  }

  private SecretKey generateSecretKey() {
    byte[] decode = Base64.getDecoder().decode(SECRET_KEY);
    return Keys.hmacShaKeyFor(decode);
  }

  public String extractUsername(String jwt) {
    Claims claims = getClaims(jwt);

    return claims.getSubject();
  }

  private Claims getClaims(String jwt) {
    return Jwts.parser()
        .verifyWith(generateSecretKey())
        .build()
        .parseSignedClaims(jwt)
        .getPayload();
  }

  public boolean isValidToken(String jwt) {
    Claims claims = getClaims(jwt);

    return claims.getExpiration().after(Date.from(Instant.now()));
  }
}
