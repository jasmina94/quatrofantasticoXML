package rs.ac.uns.ftn.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rs.ac.uns.ftn.model.database.AnalitikaIzvoda;

import java.util.Optional;

/**
 * Created by zlatan on 6/24/17.
 */
public interface AnalitikaIzvodaRepository extends JpaRepository<AnalitikaIzvoda, Long> {

    Optional<AnalitikaIzvoda> findById(Long id);

}
