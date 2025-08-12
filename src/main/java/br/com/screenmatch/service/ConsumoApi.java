package br.com.screenmatch.service;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConsumoApi {
    /**
     * Metodo para consumir uma API e retornar os dados em formato JSON
     *
     * @param endereco URL do recurso a se acessado
     * @return corpo da resposta em formato JSON
     * @throws RuntimeException se ocorrer um erro durante a requisição
     */
    public String obterDados(String endereco) {
        // HttpClient é usado para enviar requisições HTTP e receber respostas
        HttpClient client = HttpClient.newHttpClient();

        HttpRequest request = HttpRequest.newBuilder() // constrói uma nova requisição HTTP
                .uri(URI.create(endereco)) //define o URI da para dizer o endereço da requisição
                .build();

        HttpResponse<String> response = null;

        // bloco try-catch para lidar com possíveis exceções durante o envio da requisição
        try {
            response = client
                    .send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        String json = response.body();
        return json;
    }
}
