package brianpelinku.u5w3d2.services;

import brianpelinku.u5w3d2.entities.Dipendente;
import brianpelinku.u5w3d2.exceptions.UnauthorizedException;
import brianpelinku.u5w3d2.payloads.DipendenteLoginDTO;
import brianpelinku.u5w3d2.payloads.DipendenteLoginResoDTO;
import brianpelinku.u5w3d2.security.JWTTools;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private JWTTools jwtTools;

    public String checkCredenzialiAndGeneraToken(DipendenteLoginDTO body) {

        Dipendente found = this.dipendenteService.findByEmail(body.email());
        if (found.getPassword().equals(body.password())) {
            return jwtTools.createToken(found);
        } else {
            throw new UnauthorizedException("Credenziali errate");
        }
    }
}
