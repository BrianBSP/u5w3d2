package brianpelinku.u5w3d2.payloads;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;

public record NewDipendenteDTO(
        @NotEmpty(message = "username obbligatorio")
        @Size(min = 3, max = 30, message = "L'username deve essere copreso tra 3 3 30 caratteri")
        String username,
        @NotEmpty(message = "Il nome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il nome deve essere copreso tra 3 3 30 caratteri")
        String nome,
        @NotEmpty(message = "Il cognome è obbligatorio")
        @Size(min = 3, max = 30, message = "Il nome deve essere copreso tra 3 3 30 caratteri")
        String cognome,
        @Email(message = "L'email inserita non è valida.")
        String email,
        @NotEmpty(message = "Inserisci una password.")
        String password
) {
}
