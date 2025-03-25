package br.com.laudos.repository;

import br.com.laudos.domain.Estomago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EstomagoRepository extends JpaRepository<Estomago, Integer> {

    List<Estomago> findAllByOrderByIdAsc();
}
