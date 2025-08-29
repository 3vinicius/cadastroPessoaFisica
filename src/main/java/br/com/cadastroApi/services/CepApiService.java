package br.com.cadastroApi.services;

import br.com.cadastroApi.exceptions.CepInvalidoException;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Service
public class CepApiService {
    public void validarCep(String cep) {
        try {
            String cepFormatado = cep.replaceAll("-", "");

            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(new URI("https://viacep.com.br/ws/"+cepFormatado+"/json/")).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
           if (response.body().contains("\"erro\": true")){
               throw new CepInvalidoException("CEP inválido");
           }
        } catch (Exception e) {
            throw new CepInvalidoException("Erro: " + e.getMessage());
        }
    }

}
