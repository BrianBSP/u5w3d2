package brianpelinku.u5w3d2.security;

import brianpelinku.u5w3d2.entities.Dipendente;
import brianpelinku.u5w3d2.exceptions.UnauthorizedException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JWTTools {

    @Value("${jwt.secret}")
    private String secret;

    public String createToken(Dipendente dipendente) {
        // creo il token
        return Jwts.builder()
                .issuedAt(new Date(System.currentTimeMillis())) // data di creazione
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 24 * 7)) // data scadenza
                .subject(String.valueOf(dipendente.getId())) // dati dipendente (id) --> no dati sensibili
                .signWith(Keys.hmacShaKeyFor(secret.getBytes())) // firma
                .compact();

    }

    public void verificaToken(String token) {
        // controllo l'integrità del token
        try {
            Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parse(token);
        } catch (Exception e) {
            throw new UnauthorizedException("Problemi con il token.");
        }
    }

    public String estriIdDaToken(String accessToken) {
        return Jwts.parser().verifyWith(Keys.hmacShaKeyFor(secret.getBytes())).build().parseSignedClaims(accessToken).getPayload().getSubject(); // il subject è l'id del dipendente
    }
}
