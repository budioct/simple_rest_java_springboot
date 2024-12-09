package budhioct.dev.security.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class JwtService {

    @Value("${app.jwt.secretKey}")
    private String secretKey;
    @Value("${app.jwt.jwtExpirationMs}")
    private long jwtExpirationMs;
    @Value("${app.jwt.refreshTokenExpirationMs}")
    private long refreshTokenExpirationMs;
    private KeyPair keyPair;

    public String generateToken(UserDetails userDetails) {
        return generateToken(new HashMap<>(), userDetails);
    }

    public String generateToken(Map<String, Object> extraClaims, UserDetails userDetails) {
        return buildToken(extraClaims, userDetails, jwtExpirationMs);
    }

    public String generateRefreshToken(UserDetails userDetails) {
        return buildToken(new HashMap<>(), userDetails, refreshTokenExpirationMs);
    }

    private String buildToken(
            Map<String, Object> extraClaims,
            UserDetails userDetails,
            long expirationMs
    ) {
        return Jwts
                .builder()
                .setClaims(extraClaims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + expirationMs))
                .signWith(getPrivateKey(), SignatureAlgorithm.ES512)
                .compact();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername())) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    private Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts
                .parserBuilder()
                .setSigningKey(getPublicKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Key getPrivateKey() {
        return keyPair.getPrivate();
    }

    private Key getPublicKey() {
         return keyPair.getPublic();
    }

    // SignatureAlgorithm.ES512.. generates asymmetric keys, which is only suitable for HMAC algorithms such as HS256, HS384, HS512.
    private KeyPair generateKeyPair() {
        try {
            KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("EC");
            keyPairGenerator.initialize(521); // P-521 curve
            return keyPairGenerator.generateKeyPair();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error while generating ECDSA KeyPair", e);
        }
    }

    //@PostConstruct
    //private void init() {
    //    this.keyPair = generateKeyPair();
    //}

    @PostConstruct
    private void init() {
        File privateKeyFile = new File("privateKey.pem");
        File publicKeyFile = new File("publicKey.pem");

        if (privateKeyFile.exists() && publicKeyFile.exists()) {
            this.keyPair = loadKeyPair(privateKeyFile, publicKeyFile);
        } else {
            this.keyPair = generateKeyPair();
            saveKeyPair(privateKeyFile, publicKeyFile, this.keyPair);
        }
    }

    private void saveKeyPair(File privateKeyFile, File publicKeyFile, KeyPair keyPair) {
        try (FileWriter privateWriter = new FileWriter(privateKeyFile);
             FileWriter publicWriter = new FileWriter(publicKeyFile)) {
            privateWriter.write(Base64.getEncoder().encodeToString(keyPair.getPrivate().getEncoded()));
            publicWriter.write(Base64.getEncoder().encodeToString(keyPair.getPublic().getEncoded()));
        } catch (IOException e) {
            throw new RuntimeException("Error saving KeyPair", e);
        }
    }

    private KeyPair loadKeyPair(File privateKeyFile, File publicKeyFile) {
        try {
            byte[] privateKeyBytes = Base64.getDecoder().decode(Files.readString(privateKeyFile.toPath()));
            byte[] publicKeyBytes = Base64.getDecoder().decode(Files.readString(publicKeyFile.toPath()));

            KeyFactory keyFactory = KeyFactory.getInstance("EC");
            PrivateKey privateKey = keyFactory.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            PublicKey publicKey = keyFactory.generatePublic(new X509EncodedKeySpec(publicKeyBytes));

            return new KeyPair(publicKey, privateKey);
        } catch (Exception e) {
            throw new RuntimeException("Error loading KeyPair", e);
        }
    }

}
