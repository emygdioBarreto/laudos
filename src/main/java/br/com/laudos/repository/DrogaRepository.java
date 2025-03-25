package br.com.laudos.repository;

import br.com.laudos.domain.Droga;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DrogaRepository extends JpaRepository<Droga, Integer> {

    List<Droga> findAllByOrderByIdAsc();
}
