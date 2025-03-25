package br.com.laudos.repository;

import br.com.laudos.domain.Resumo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ResumoRepository extends JpaRepository<Resumo, Integer> {

    List<Resumo> findAllByOrderByIdAsc();
}
