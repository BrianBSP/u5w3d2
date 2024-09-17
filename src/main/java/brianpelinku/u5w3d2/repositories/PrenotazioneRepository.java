package brianpelinku.u5w3d2.repositories;

import brianpelinku.u5w3d2.entities.Dipendente;
import brianpelinku.u5w3d2.entities.Prenotazione;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.Optional;

@Repository
public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Integer> {

    Optional<Prenotazione> findByDipendenteAndViaggioDataPrenotazione(Dipendente dipendente, LocalDate dataPrenotazione);
}
