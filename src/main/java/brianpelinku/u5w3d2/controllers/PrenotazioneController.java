package brianpelinku.u5w3d2.controllers;

import brianpelinku.u5w3d2.entities.Prenotazione;
import brianpelinku.u5w3d2.exceptions.BadRequestException;
import brianpelinku.u5w3d2.payloads.NewPrenotazioneDTO;
import brianpelinku.u5w3d2.payloads.NewPrenotazioneRespDTO;
import brianpelinku.u5w3d2.services.PrenotazioneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("prenotazioni")
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    // POST
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NewPrenotazioneRespDTO createPrenotazione(@RequestBody @Validated NewPrenotazioneDTO body, BindingResult validation) {
        if (validation.hasErrors()) {
            String messages = validation
                    .getAllErrors()
                    .stream()
                    .map(error -> error.getDefaultMessage())
                    .collect(Collectors.joining(". "));
            throw new BadRequestException("Segnalazione Errori nel Payload. " + messages);
        } else {
            return new NewPrenotazioneRespDTO(this.prenotazioneService.savePrenotazione(body).prenotazioneId());
        }
    }

    // GET All
    @GetMapping
    public Page<Prenotazione> getAllPrenotazioni(@RequestParam(defaultValue = "0") int page,
                                                 @RequestParam(defaultValue = "5") int size,
                                                 @RequestParam(defaultValue = "id") String sortBy) {
        return this.prenotazioneService.findAll(page, size, sortBy);
    }

    // GET byId
    @GetMapping("/{prenotazioneID}")
    public NewPrenotazioneRespDTO findPrenotazioneById(@PathVariable int prenotazioneID) {
        return new NewPrenotazioneRespDTO(this.prenotazioneService.findById(prenotazioneID).getId());
    }

    // DELETE
    @DeleteMapping("/{prenotazioneID}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void findByIdAndDelete(@PathVariable int prenotazioneID) {
        prenotazioneService.findByIdAndDelete(prenotazioneID);
    }


}
