package br.com.screenmatch.main;

import br.com.screenmatch.model.DadosSerie;
import br.com.screenmatch.model.DadosTemporada;
import br.com.screenmatch.service.ConsumoApi;
import br.com.screenmatch.service.ConverteDados;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    private final Scanner sc = new Scanner(System.in);
    private final ConsumoApi consumoApi = new ConsumoApi();
    private final ConverteDados conversor = new ConverteDados();
    private final String URL_BASE = "https://www.omdbapi.com/?t=";
    private final String API_KEY = "&apikey=6585022c";


    public void menu() {
        System.out.println("digite o nome de uma série para buscar");
        var nomeSerie = sc.nextLine();
        var json = consumoApi.getDados(URL_BASE + nomeSerie.replace(" ", "+") + API_KEY);

        var dados = conversor.obterDados(json, DadosSerie.class);
        System.out.println(dados);

        List<DadosTemporada> temporadas = new ArrayList<>();
        for (int i = 1; i <= dados.totalTemps(); i++) {
            json = consumoApi.getDados(URL_BASE + nomeSerie.replace(" ", "+") + "&season=" + i + API_KEY);
            var dadosTemporadas = conversor.obterDados(json, DadosTemporada.class);
            temporadas.add(dadosTemporadas);
        }

        temporadas.forEach(System.out::println);

        temporadas.forEach(t -> t.episodios().forEach(e -> System.out.println(e.titulo())));

//        for(int i = 0; i < dados.totalTemps(); i++){
//            List<DadosEpisodio> episodiosTemp = temporadas.get(i).episodios();
//            for (int j = 0; j < episodiosTemp.size(); j++) {
//                System.out.println(episodiosTemp.get(j).titulo());
//            }
//        }
    }
}
