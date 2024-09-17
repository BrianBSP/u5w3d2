package brianpelinku.u5w3d2.entities;

import brianpelinku.u5w3d2.enums.StatoViaggio;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@Entity
@Table(name = "viaggi")
public class Viaggio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Setter(AccessLevel.NONE)
    private int id;
    private String destinazione;
    @Column(name = "data_prenotazione")
    private LocalDate dataPrenotazione;
    @Column(name = "stato_viaggio")
    @Enumerated(EnumType.STRING)
    private StatoViaggio statoViaggio;
}
