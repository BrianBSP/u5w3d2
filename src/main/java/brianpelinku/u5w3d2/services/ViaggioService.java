package brianpelinku.u5w3d2.services;

import brianpelinku.u5w3d2.entities.Viaggio;
import brianpelinku.u5w3d2.enums.StatoViaggio;
import brianpelinku.u5w3d2.exceptions.NotFoundException;
import brianpelinku.u5w3d2.payloads.NewDipendenteRespDTO;
import brianpelinku.u5w3d2.payloads.NewViaggioDTO;
import brianpelinku.u5w3d2.repositories.ViaggioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
public class ViaggioService {

    @Autowired
    private ViaggioRepository viaggioRepository;

    // salvo nuovo viaggio nel DB --> post + body
    public NewDipendenteRespDTO saveViaggio(NewViaggioDTO body) {

        Viaggio newViaggio = new Viaggio();
        newViaggio.setDestinazione(body.destinazione());
        newViaggio.setDataPrenotazione(LocalDate.parse(body.dataPrenotazione()));
        newViaggio.setStatoViaggio(StatoViaggio.valueOf(body.statoViaggio()));


        // salvo il nuovo record
        return new NewDipendenteRespDTO(this.viaggioRepository.save(newViaggio).getId());
    }

    // find All
    public Page<Viaggio> findAll(int page, int size, String sortBy) {
        if (page > 20) page = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.viaggioRepository.findAll(pageable);
    }

    // cerco viaggio byId
    public Viaggio findById(int viaggioId) {
        return this.viaggioRepository.findById(viaggioId).orElseThrow(() -> new NotFoundException(viaggioId));
    }

    // delete Viaggio
    public void findByIdAndDelete(int viaggioId) {
        Viaggio trovato = this.findById(viaggioId);
        this.viaggioRepository.delete(trovato);
    }
}
