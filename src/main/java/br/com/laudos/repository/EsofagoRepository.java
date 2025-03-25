package br.com.laudos.repository;

import br.com.laudos.domain.Esofago;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EsofagoRepository extends JpaRepository<Esofago, Integer> {

    List<Esofago> findAllByOrderByIdAsc();
}
