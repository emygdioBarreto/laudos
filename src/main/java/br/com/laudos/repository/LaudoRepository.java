package br.com.laudos.repository;

import br.com.laudos.domain.Laudo;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface LaudoRepository extends JpaRepository<Laudo, Long> {

    List<Laudo> findAllByOrderByIdAsc();
}
