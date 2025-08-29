package br.com.cadastroApi.exceptions;

public class CepInvalidoException extends RuntimeException {

    public CepInvalidoException(String message) {
        super(message);
    }

}
