package br.com.laudos.repository;

import br.com.laudos.domain.Premedicacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PremedicacaoRepository extends JpaRepository<Premedicacao, Integer> {

    List<Premedicacao> findAllByOrderByIdAsc();

    @Query(value = "SELECT * FROM PREMEDICACAO ORDER BY premedicacao asc", nativeQuery = true)
    List<Premedicacao> findAllPremedicacoes();

}
