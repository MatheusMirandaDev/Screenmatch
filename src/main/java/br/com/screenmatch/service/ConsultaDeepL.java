package br.com.screenmatch.service;

import com.deepl.api.DeepLClient;
import com.deepl.api.TextResult;

public class ConsultaDeepL {

    public static String obterTraducao(String texto) {
        try {

        String authKey = System.getenv("DEEPL_AUTH_KEY");
        if (authKey == null || authKey.isBlank()) {
            throw new RuntimeException("Chave da API DeepL não encontrada. Defina a variável de ambiente DEEPL_AUTH_KEY.");
        }

        DeepLClient client = new DeepLClient(authKey);
        TextResult result = client.translateText(texto, null, "pt-BR");

        return result.getText();
 
        } catch (Exception ex) {
            ex.printStackTrace();
            return texto;
        }
    }
}