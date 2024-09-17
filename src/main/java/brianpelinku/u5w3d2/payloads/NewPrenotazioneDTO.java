package brianpelinku.u5w3d2.payloads;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record NewPrenotazioneDTO(
        @NotEmpty(message = "La data di richiesta della prenotazione è obbligatoria.")
        @Size(min = 10, max = 10)
        String dataDiRichiesta,
        @NotEmpty(message = "Inserire delle note. Campo aggiuntivo.")
        @Size(min = 1, max = 300)
        String noteAggiuntive,
        @NotNull(message = "Campo obbligatorio. Indicare id dipendente.")
        int dipendenteId,
        @NotNull(message = "Campo obbligatorio. Indicare id viaggio.")
        int viaggioId
) {
}
