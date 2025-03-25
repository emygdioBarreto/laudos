package br.com.laudos.repository;

import br.com.laudos.domain.ObservacaoClinica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacaoClinicaRepository extends JpaRepository<ObservacaoClinica, Integer> {

    List<ObservacaoClinica> findAllByOrderByIdAsc();
}
