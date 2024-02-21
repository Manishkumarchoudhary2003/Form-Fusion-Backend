package Manish.FormFusion.filter;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.*;
import java.util.Objects;
import java.util.function.Function;


@Service
public class JwtService {

    private static final String SECRET_KEY = "!@#$FDGSDFGSGSGSGSHSHSHSSHGFFDSGSFGSSGHSDFSDFSFSFKKKJHJJGJGJHGJHFGDFDGFDGFHCHCBVBHHJGJHFGSFSDFSFSFSF";
    private static final long EXPIRATION_TIME = 864_000_000; // 10 days

    public static String generateToken(String email) {
        Map<String, Objects> claims = new HashMap<>();
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(email)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(getSignKey(),SignatureAlgorithm.HS256)
                .compact();
    }

    private static Key getSignKey() {
        byte[] KeyByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(KeyByte);
    }

    public String extractUserName(String token){
        return extractClaim(token,Claims::getSubject);
    }

    public Date extractExpiration(String token){
        return extractClaim(token,Claims::getExpiration);
    }
    private <T> T extractClaim(String token, Function<Claims,T> claimResolver) {
        final Claims claims = extractAllClaims(token);
        return claimResolver.apply(claims);
    }

    private Claims extractAllClaims(String token){
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    private Boolean isTokenExpired(String token){
        return extractExpiration(token).before(new Date());
    }
    public Boolean validateToken(String token, UserDetails userDetails){
        final String userName= extractUserName(token);
        return (userName.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}

