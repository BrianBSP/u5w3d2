package brianpelinku.u5w3d2.controllers;

import brianpelinku.u5w3d2.entities.Dipendente;
import brianpelinku.u5w3d2.payloads.NewDipendenteDTO;
import brianpelinku.u5w3d2.payloads.NewDipendenteRespDTO;
import brianpelinku.u5w3d2.services.DipendenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/dipendenti")
public class DipendenteController {

    @Autowired
    private DipendenteService dipendenteService;


    // GET All
    @GetMapping
    @PreAuthorize("hasAuthority('ADMIN')") // solo gli admin possono leggere l'elenco dei dipendenti
    public Page<Dipendente> getAllDipendenti(@RequestParam(defaultValue = "0") int page,
                                             @RequestParam(defaultValue = "5") int size,
                                             @RequestParam(defaultValue = "id") String sortBy) {
        return this.dipendenteService.findAll(page, size, sortBy);
    }

    // creo un nuovo profilo per un nuovo dipendente
    /*@PostMapping("/register")
    @PreAuthorize("hasAuthority('ADMIN')") // solo gli admin possono registrare nuovi dipendenti
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
    }*/

    // GET byId
    @GetMapping("/{dipendenteId}")
    public NewDipendenteRespDTO findDipendenteById(@PathVariable int dipendenteId) {
        return new NewDipendenteRespDTO(this.dipendenteService.findById(dipendenteId).getId());
    }

    // PUT   +body
    @PutMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')") // solo gli admin possono modificare i dipendenti
    public NewDipendenteRespDTO findByIdAndUpdate(@PathVariable int dipendenteId, @RequestBody NewDipendenteDTO newDipendente) {
        return new NewDipendenteRespDTO(this.dipendenteService.findByIdAndUpdate(dipendenteId, newDipendente).dipendenteId());
    }

    // DELETE
    @DeleteMapping("/{dipendenteId}")
    @PreAuthorize("hasAuthority('ADMIN')") // solo gli admin possono elimini i dipendenti
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable int dipendenteId) {
        dipendenteService.findByIdAndDelete(dipendenteId);
    }

    // UPLOAD IMMAGINE
    @PostMapping("/{dipendenteId}/avatar")
    public Dipendente uploadImage(@RequestParam("avatar") MultipartFile img, @PathVariable int dipendenteId) throws IOException {
        try {
            return this.dipendenteService.uploadImagine(img, dipendenteId);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    // dopo aver effettuato il login posso vedere il mio profilo
    @GetMapping("/me")
    public Dipendente getProfile(@AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        return dipendenteAutenticato;
    }

    // dopo aver effettuato il login posso modificare il mio profilo
    @PutMapping("/me")
    public NewDipendenteRespDTO updateProfile(@AuthenticationPrincipal Dipendente dipendenteAutenticato, @RequestBody NewDipendenteDTO body) {
        return this.dipendenteService.findByIdAndUpdate(dipendenteAutenticato.getId(), body);
    }

    // dopo aver effettuato il login posso eliminare il mio profilo
    @DeleteMapping("/me")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteProfile(@AuthenticationPrincipal Dipendente dipendenteAutenticato) {
        this.dipendenteService.findByIdAndDelete(dipendenteAutenticato.getId());
    }

}
