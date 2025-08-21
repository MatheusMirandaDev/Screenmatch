package br.com.screenmatch.main;

import br.com.screenmatch.model.*;
import br.com.screenmatch.repository.SerieRepository;
import br.com.screenmatch.service.ConsumoApi;
import br.com.screenmatch.service.ConverteDados;

import java.util.*;
import java.util.stream.Collectors;

public class Main {

    private final String ENDERECO = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";
    private Scanner leitura = new Scanner(System.in);
    private ConsumoApi consumo = new ConsumoApi();
    private ConverteDados conversor = new ConverteDados();
    private List<DadosSerie> dadosSeries = new ArrayList<>();

    private SerieRepository repository;

    private List<Serie> series = new ArrayList<>();

    public Main(SerieRepository repository) {
        this.repository = repository;
    }

    public void exibeMenu() {

        var opcao = -1;
        do {

            var menu = """
                    1 - Buscar séries
                    2 - Buscar episódios
                    3 - Listar series
                    4- Buscar série por título
                    5- Buscar séries por ator
                    6- Buscar top 5 séries
                    7- Buscar séries por categoria
                    8- Filtrar séries por avaliação
                    0 - Sair
                    """;

            System.out.println(menu);
            opcao = leitura.nextInt();
            leitura.nextLine();

            switch (opcao) {
                case 1:
                    buscarSerieWeb();
                    System.out.println();
                    break;
                case 2:
                    buscarEpisodioPorSerie();
                    System.out.println();
                    break;
                case 3:
                    listarSeriesBuscadas();
                    System.out.println();
                    break;
                case 4:
                    buscarSeriePorTitulo();
                    System.out.println();
                    break;
                case 5:
                    buscarSeriesPorAtor();
                    System.out.println();
                    break;
                case 6:
                    buscarTop5Series();
                    System.out.println();
                    break;
                case 7:
                    buscarSeriesPorCategoria();
                    System.out.println();
                    break;
                case 8:
                    filtrarSeriesPorTemporadaEAvaliacao();
                    System.out.println();
                    break;

                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida");
            }
        } while (opcao != 0);
    }

    private void buscarSerieWeb() {
        DadosSerie dados = getDadosSerie();
        Serie serie = new Serie(dados);
        repository.save(serie);
        System.out.println(dados);
    }

    private DadosSerie getDadosSerie() {
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();
        var json = consumo.obterDados(ENDERECO + nomeSerie.replace(" ", "+") + API_KEY);
        DadosSerie dados = conversor.obterDados(json, DadosSerie.class);
        return dados;
    }

    private void buscarEpisodioPorSerie() {
        listarSeriesBuscadas();

        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            var serieEncontrada = serieBuscada.get();

            List<DadosTemporada> temporadas = new ArrayList<>();

            for (int i = 1; i <= serieEncontrada.getTotalTemporadas(); i++) {
                var json = consumo.obterDados(ENDERECO + serieEncontrada.getTitulo().replace(" ", "+") + "&season=" + i + API_KEY);
                DadosTemporada dadosTemporada = conversor.obterDados(json, DadosTemporada.class);
                temporadas.add(dadosTemporada);
            }

            temporadas.forEach(System.out::println);

            List<Episodio> episodios = temporadas.stream()
                    .flatMap(dt -> dt.episodios().stream()
                            .map(e -> new Episodio(dt.numero(), e)))
                    .collect(Collectors.toList());

            serieEncontrada.setEpisodios(episodios);

            repository.save(serieEncontrada);
        }else {
            System.out.println("Série não encontrada.");
        }
    }

    private void listarSeriesBuscadas() {
        series = repository.findAll();

        series.stream()
                .sorted(Comparator.comparing(Serie::getGenero))
                .forEach(System.out::println);
    }

    private void buscarSeriePorTitulo(){
        System.out.println("Digite o nome da série para busca");
        var nomeSerie = leitura.nextLine();

        Optional<Serie> serieBuscada = repository.findByTituloContainingIgnoreCase(nomeSerie);

        if (serieBuscada.isPresent()) {
            System.out.println("dados da serie: " + serieBuscada.get());
        } else {
            System.out.println("serie não encontrada");
        }

    }

    private void buscarSeriesPorAtor(){
        System.out.println("Digite o nome do ator");
        var ator = leitura.nextLine();

        var seriesEncontradas = repository.findByAtoresContainingIgnoreCase(ator);

        System.out.println("series em que " + ator + " trabalhou: ");
        seriesEncontradas.forEach(s ->
                System.out.println(s.getTitulo()));
    }

    private void buscarTop5Series(){
        var topSeries = repository.findTop5ByOrderByAvaliacaoDesc();

        topSeries.forEach(s ->
                System.out.println(s.getTitulo() + " avaliação: " + s.getAvaliacao()));

    }

    private void buscarSeriesPorCategoria(){
        System.out.println();
        var nomeGenero = leitura.nextLine();
        var categoria = Categoria.fromPortugues(nomeGenero);

        List<Serie> seriesPorCategoria = repository.findByGenero(categoria);

        seriesPorCategoria.forEach(System.out::println);
    }

    private void filtrarSeriesPorTemporadaEAvaliacao(){
        System.out.println("Filtar séries até quantas temporadas? ");
        var totalTemporadas = leitura.nextInt();
        leitura.nextLine();
        System.out.println("Com avaliação a partir de qual valor?");
        var avaliacao = leitura.nextDouble();
        List<Serie> filtroSerie = repository.seriesPorTemporadaEavaliacao(totalTemporadas, avaliacao);
        System.out.println("*** Series Filtradas ***");
        filtroSerie.forEach(s ->
                System.out.println(s.getTitulo() + " - avaliacao: " + s.getAvaliacao()));
    }
}