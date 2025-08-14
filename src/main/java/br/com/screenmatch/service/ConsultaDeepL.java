package br.com.screenmatch.service;

import com.deepl.api.TextResult;
import com.deepl.api.Translator;

public class ConsultaDeepL {

    public static String obterTraducao(String texto) {
        try {
            // Lê a chave da variável de ambiente (não deixar fixa no código)
            String authKey = System.getenv("DEEPL_AUTH_KEY");

            if (authKey == null || authKey.isBlank()) {
                throw new RuntimeException("Chave da API DeepL não encontrada. Defina a variável de ambiente DEEPL_AUTH_KEY.");
            }

            // Cria o cliente DeepL
            Translator translator = new Translator(authKey);

            // Traduz para pt-BR (detecção automática de idioma de origem)
            TextResult result = translator.translateText(texto, null, "pt-BR");

            return result.getText();

        } catch (Exception e) {
            e.printStackTrace();
            // Caso ocorra erro, retorna o texto original como fallback
            return texto;
        }
    }
}