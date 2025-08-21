package br.com.screenmatch.repository;

import br.com.screenmatch.model.Categoria;
import br.com.screenmatch.model.Episodio;
import br.com.screenmatch.model.Serie;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface SerieRepository extends JpaRepository<Serie, Long> {

    // JPA Derived Query Methods
    Optional<Serie> findByTituloContainingIgnoreCase(String serie);
    List<Serie> findByAtoresContainingIgnoreCase(String ator);
    List<Serie> findTop5ByOrderByAvaliacaoDesc();
    List<Serie> findByGenero(Categoria categoria);
//    List<Serie> findByTotalTemporadasLessThanEqualAndAvaliacaoGreaterThanEqual(int totalTemporada, double avaliacao);

    //Java Persistence query language (JPQL) "manual"
    @Query("SELECT s\n" +
            "FROM Serie s\n" +
            "WHERE totalTemporadas <= :totalTemporada\n" +
            "and avaliacao >= :avaliacao")
    List<Serie> seriesPorTemporadaEavaliacao(int totalTemporada, double avaliacao);

    @Query("SELECT s FROM Serie s JOIN s.episodios e WHERE e.titulo ILIKE %:trechoEpisodio%")
    List<Episodio> episodiosPorTrecho(String trechoEpisodio);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie ORDER BY e.avaliacao DESC LIMIT 5")
    List<Episodio> topEpisodiosPorSerie(Serie serie);

    @Query("SELECT e FROM Serie s JOIN s.episodios e WHERE s = :serie AND YEAR(e.dataLancamento) >= :anoLancamento")
    List<Episodio> episodiosPorSerieEAno(Serie serie, int anoLancamento);
}
