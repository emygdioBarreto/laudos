package br.com.laudos.repository;

import br.com.laudos.domain.Observacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ObservacaoRepository extends JpaRepository<Observacao, Integer> {

    List<Observacao> findAllByOrderByIdAsc();
}
