package com.ragilwiradiputra.sawitpro.configuration;

import com.ragilwiradiputra.sawitpro.model.User;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.*;
import java.util.function.Function;

@Component
public class JwtComponent {

  @Value("${jwt.authentication.private-key}")
  private String privateKeyPath;

  private PrivateKey privateKey;

  @PostConstruct
  public void init() throws NoSuchAlgorithmException, InvalidKeySpecException {
    InputStream inputStream = getClass().getClassLoader().getResourceAsStream(privateKeyPath);

    if (inputStream != null) {
      Scanner scanner = new Scanner(inputStream).useDelimiter("\\A");
      String privateKeyString = scanner.hasNext() ? scanner.next() : "";
      scanner.close();

      // Parse the private key string
      privateKeyString = privateKeyString
          .replace("-----BEGIN PRIVATE KEY-----", "")
          .replace("-----END PRIVATE KEY-----", "")
          .replaceAll("\\s", "");
      byte[] privateKeyBytes = Base64.getDecoder().decode(privateKeyString);
      PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(privateKeyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("RSA");
      this.privateKey = keyFactory.generatePrivate(keySpec);
    }

  }

  public String extractUsername(String token) {
    return extractClaim(token, Claims::getSubject);
  }

  public Date extractExpiration(String token) {
    return extractClaim(token, Claims::getExpiration);
  }

  public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
    final Claims claims = extractAllClaims(token);
    return claimsResolver.apply(claims);
  }

  private Claims extractAllClaims(String token) {
    JwtParser jwtParser = Jwts.parserBuilder()
        .setSigningKey(privateKey).build();
    return jwtParser.parseClaimsJws(token).getBody();
  }

  private Boolean isTokenExpired(String token) {
    return extractExpiration(token).before(new Date());
  }

  public String generateToken(User userDetails) {
    Map<String, Object> claims = new HashMap<>();
    claims.put("name", userDetails.getName());
    claims.put("authorities",userDetails.getAuthorities());
    return createToken(claims, userDetails.getUsername());
  }

  private String createToken(Map<String, Object> claims, String subject) {

    return Jwts.builder().setClaims(claims)
        .setSubject(subject)
        .setIssuedAt(new Date(System.currentTimeMillis()))
        .setHeaderParam("alg", "RS256")
        .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10))
        .signWith(privateKey).compact();

  }

  public Boolean validateToken(String token, User userDetails) {
    final String username = extractUsername(token);
    return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
  }

}
