package brianpelinku.u5w3d2.services;

import brianpelinku.u5w3d2.entities.Dipendente;
import brianpelinku.u5w3d2.exceptions.BadRequestException;
import brianpelinku.u5w3d2.exceptions.NotFoundException;
import brianpelinku.u5w3d2.payloads.NewDipendenteDTO;
import brianpelinku.u5w3d2.payloads.NewDipendenteRespDTO;
import brianpelinku.u5w3d2.repositories.DipendenteRepository;
import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
public class DipendenteService {

    @Autowired
    private DipendenteRepository dipendenteRepository;

    @Autowired
    private Cloudinary cloudinary;

    // salvo nuovo dipendente nel DB --> post + body
    public Dipendente saveDipendente(NewDipendenteDTO body) {

        this.dipendenteRepository.findByEmail(body.email()).ifPresent(author -> {
            throw new BadRequestException("L'email " + body.email() + " è già in uso.");
        });
        Dipendente newDip = new Dipendente(body.username(), body.nome(), body.cognome(), body.email(), body.password(),
                "https://ui-avatars.com/api/?name=" + body.nome() + "+" + body.cognome());

        // salvo il nuovo record
        return this.dipendenteRepository.save(newDip);
    }

    // cerco tutti i dipendenti
    public Page<Dipendente> findAll(int page, int size, String sortBy) {
        if (page > 20) page = 20;
        Pageable pageable = PageRequest.of(page, size, Sort.by(sortBy));
        return this.dipendenteRepository.findAll(pageable);
    }

    // cerco dipendente byId
    public Dipendente findById(int dipendenteId) {
        return this.dipendenteRepository.findById(dipendenteId).orElseThrow(() -> new NotFoundException(dipendenteId));
    }

    // update dipendente
    public NewDipendenteRespDTO findByIdAndUpdate(int dipendenteId, NewDipendenteDTO newDipendente) {

        // rifaccio controllo sulla email se è già salvata nel db
        this.dipendenteRepository.findByEmail(newDipendente.email()).ifPresent(author -> {
            throw new BadRequestException("L'email " + newDipendente.email() + " è già in uso.");
        });
        Dipendente trovato = this.findById(dipendenteId);
        trovato.setUsername(newDipendente.username());
        trovato.setNome(newDipendente.nome());
        trovato.setCognome(newDipendente.cognome());
        trovato.setEmail(newDipendente.email());
        trovato.setAvatar("https://ui-avatars.com/api/?name=" + newDipendente.nome() + "+" + newDipendente.cognome());

        return new NewDipendenteRespDTO(this.dipendenteRepository.save(trovato).getId());
    }

    // delete dipendente
    public void findByIdAndDelete(int dipendenteId) {
        Dipendente trovato = this.findById(dipendenteId);
        this.dipendenteRepository.delete(trovato);
    }

    // upload immagine
    public Dipendente uploadImagine(MultipartFile file, int dipendenteId) throws IOException {
        Dipendente trovato = this.findById(dipendenteId);
        String url = (String) cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap()).get("url");

        trovato.setAvatar(url);

        return dipendenteRepository.save(trovato);
    }

    public Dipendente findByEmail(String email) {
        return dipendenteRepository.findByEmail(email).orElseThrow(() -> new NotFoundException("L'utente con l'email " + email + " non è stato trovato."));
    }

}
