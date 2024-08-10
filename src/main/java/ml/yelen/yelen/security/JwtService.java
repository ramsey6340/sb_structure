package ml.yelen.yelen.security;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import ml.yelen.yelen.entities.AdminRole;
import ml.yelen.yelen.entities.Administrator;
import ml.yelen.yelen.exceptions.BadRequestException;
import ml.yelen.yelen.exceptions.NotFoundException;
import ml.yelen.yelen.repositories.AdminRepository;
import ml.yelen.yelen.resources.ConstanteValues;
import ml.yelen.yelen.resources.ErrorMessageValue;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Component
@Slf4j
public class JwtService {

    private final AdminRepository adminRepository;

    public JwtService(AdminRepository authInfoRepository) {
        this.adminRepository = authInfoRepository;
    }
    public String extractUsername(String token) {
        try {
            return extractClaim(token, Claims::getSubject);
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public Date extractExpiration(String token) {
        try {
            return extractClaim(token, Claims::getExpiration);
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        try {
            final Claims claims = extractAllClaims(token);
            return claimsResolver.apply(claims);
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private Claims extractAllClaims(String token) {
       try {
           return Jwts
                   .parserBuilder()
                   .setSigningKey(getSignKey())
                   .build()
                   .parseClaimsJws(token)
                   .getBody();
       }catch (Exception e) {
           throw new BadRequestException(e.getMessage());
       }
    }

    private Boolean isTokenExpired(String token) {
        try {
            return extractExpiration(token).before(new Date());
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
       try {
           final String username = extractUsername(token);
           return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
       }catch (Exception e) {
           throw new BadRequestException(e.getMessage());
       }
    }



    public String GenerateToken(String username){
       try {
           Optional<Administrator> adminOptional = adminRepository.findByUsername(username);
           if(adminOptional.isPresent()) {
               Map<String, Object> claims = new ObjectMapper().convertValue(adminOptional.get(), new TypeReference<Map<String, Object>>() {});
               claims.put("roles", adminOptional.get().getRoles().stream().map(AdminRole::getName).toList());
               return createToken(claims, username);
           }
           throw new NotFoundException(ErrorMessageValue.USER_NOT_FOUND);

       }catch (Exception e) {
           throw new BadRequestException(e.getMessage());
       }
    }

    public String GenerateRefreshToken(String username){
        try {
            Optional<Administrator> userInfoOptional = adminRepository.findByUsername(username);
            if(userInfoOptional.isPresent()) {
                return createRefreshToken(username);
            }
            throw new NotFoundException(ErrorMessageValue.USER_NOT_FOUND);

        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }



    private String createToken(Map<String, Object> claims, String username) {
        try {
            return Jwts.builder()
                    .setClaims(claims)
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()+ ConstanteValues.TOKEN_LIFETIME))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private String createRefreshToken(String username) {
        try {
            return Jwts.builder()
                    .setSubject(username)
                    .setIssuedAt(new Date(System.currentTimeMillis()))
                    .setExpiration(new Date(System.currentTimeMillis()+ ConstanteValues.REFRESH_TOKEN_LIFETIME))
                    .signWith(getSignKey(), SignatureAlgorithm.HS256).compact();
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }

    private Key getSignKey() {
        try {
            byte[] keyBytes = Decoders.BASE64.decode(ConstanteValues.SECRET);
            return Keys.hmacShaKeyFor(keyBytes);
        }catch (Exception e) {
            throw new BadRequestException(e.getMessage());
        }
    }
}