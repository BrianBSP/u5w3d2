package brianpelinku.u5w3d2.controllers;

import brianpelinku.u5w3d2.exceptions.BadRequestException;
import brianpelinku.u5w3d2.payloads.DipendenteLoginDTO;
import brianpelinku.u5w3d2.payloads.DipendenteLoginResoDTO;
import brianpelinku.u5w3d2.payloads.NewDipendenteDTO;
import brianpelinku.u5w3d2.payloads.NewDipendenteRespDTO;
import brianpelinku.u5w3d2.services.AuthService;
import brianpelinku.u5w3d2.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private AuthService authService;

    @Autowired
    private DipendenteService dipendenteService;

    @PostMapping("/login")
    public DipendenteLoginResoDTO login(@RequestBody DipendenteLoginDTO body) {
        return new DipendenteLoginResoDTO(this.authService.checkCredenzialiAndGeneraToken(body));
    }

    // POST --> creo un nuovo record --- +body
    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public NewDipendenteRespDTO createDipendente(@RequestBody @Validated NewDipendenteDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            String messages = validation
                    .getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Segnalazione Errori nel Payload. " + messages);
        } else {
            return new NewDipendenteRespDTO(this.dipendenteService.saveDipendente(body).getId());
        }
    }
}
