package br.com.screenmatch.repository;

import br.com.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {
    Optional<Serie> findByTituloContainingIgnoreCase(String serie);

    List<Serie> findByAtoresContainingIgnoreCase(String ator);
}
