package brianpelinku.u5w3d2.security;

import brianpelinku.u5w3d2.entities.Dipendente;
import brianpelinku.u5w3d2.services.DipendenteService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.UnavailableException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JWTCheckFilter extends OncePerRequestFilter {

    @Autowired
    private JWTTools jwtTools;

    @Autowired
    private DipendenteService dipendenteService;

    // per filtrare ogni richiesta
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        // prima verifico se c'è l'Authorization Header
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer "))
            throw new UnavailableException("Inserire correttamente il Token.");

        // lo estrapoliamo
        String accessToken = authHeader.substring(7);
        System.out.println("access Token: " + accessToken);
        // controlliamo che sia un token "valido"
        jwtTools.verificaToken(accessToken);

        // cerco il dipendente tramite id (id all'interno del token)
        String id = jwtTools.estriIdDaToken(accessToken);
        Dipendente dipendenteCorrente = dipendenteService.findById(Integer.parseInt(id));

        // aggiorno il Security Context per informare Spring chi sta facendo la richiesta (che ruolo ha)
        Authentication authentication = new UsernamePasswordAuthenticationToken(dipendenteCorrente, null, dipendenteCorrente.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        // passo allo step successivo
        filterChain.doFilter(request, response);
    }

    // per non filtrare la richiesta
    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {

        return new AntPathMatcher().match("/auth/**", request.getServletPath());
    }
}
