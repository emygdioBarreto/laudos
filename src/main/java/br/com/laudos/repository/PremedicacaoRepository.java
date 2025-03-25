package br.com.laudos.repository;

import br.com.laudos.domain.Premedicacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PremedicacaoRepository extends JpaRepository<Premedicacao, Integer> {

    List<Premedicacao> findAllByOrderByIdAsc();
}
