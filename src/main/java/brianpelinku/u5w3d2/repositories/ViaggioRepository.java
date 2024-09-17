package brianpelinku.u5w3d2.repositories;

import brianpelinku.u5w3d2.entities.Viaggio;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ViaggioRepository extends JpaRepository<Viaggio, Integer> {
}
