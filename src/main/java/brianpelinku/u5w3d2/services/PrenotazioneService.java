package brianpelinku.u5w3d2.services;

import brianpelinku.u5w3d2.entities.Dipendente;
import brianpelinku.u5w3d2.entities.Prenotazione;
import brianpelinku.u5w3d2.entities.Viaggio;
import brianpelinku.u5w3d2.exceptions.BadRequestException;
import brianpelinku.u5w3d2.exceptions.NotFoundException;
import brianpelinku.u5w3d2.payloads.NewPrenotazioneDTO;
import brianpelinku.u5w3d2.payloads.NewPrenotazioneRespDTO;
import brianpelinku.u5w3d2.repositories.PrenotazioneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class PrenotazioneService {
    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private DipendenteService dipendenteService;

    @Autowired
    private ViaggioService viaggioService;

    // salvo nuovo viaggio nel DB --> post + body
    public NewPrenotazioneRespDTO savePrenotazione(NewPrenotazioneDTO body) {

        Dipendente dipendente = dipendenteService.findById(body.dipendenteId());
        Viaggio viaggio = viaggioService.findById(body.viaggioId());

        prenotazioneRepository.findByDipendenteAndViaggioDataPrenotazione(dipendente, viaggio.getDataPrenotazione()).ifPresent(
                prenotazione -> {
                    throw new BadRequestException("Il dipendente con id " + dipendente.getId() + " ha già in programma un viaggio nella data richiesta.");
                }
        );

        Prenotazione prenotazione = new Prenotazione();
        prenotazione.setDataDiRichiesta(LocalDate.parse(body.dataDiRichiesta()));
        prenotazione.setNoteAggiuntive(body.noteAggiuntive());
        prenotazione.setViaggio(viaggio);
        prenotazione.setDipendente(dipendente);


        // salvo il nuovo record
        return new NewPrenotazioneRespDTO(this.prenotazioneRepository.save(prenotazione).getId());
    }

    // finAll
    public Page<Prenotazione> findAll(int page, int size, String sortBy) {
        if (page > 20) page = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.prenotazioneRepository.findAll(pageable);
    }

    // cerco Prenotazione byId
    public Prenotazione findById(int prenotazioneId) {
        return this.prenotazioneRepository.findById(prenotazioneId).orElseThrow(() -> new NotFoundException(prenotazioneId));
    }

    // delete Prenotazione
    public void findByIdAndDelete(int prenotazioneId) {
        Prenotazione trovato = this.findById(prenotazioneId);
        this.prenotazioneRepository.delete(trovato);
    }
}
