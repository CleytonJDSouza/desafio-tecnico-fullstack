package com.example.desafio_tecnico.service;

import com.example.desafio_tecnico.exception.CepInvalidoException;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;

@Service
public class CepValidationService {

    private final ViaCepClient viaCepClient;

    public CepValidationService(ViaCepClient viaCepClient) {
        this.viaCepClient = viaCepClient;
    }

    public ViaCepClient.ViaCepResponse validar(String cep) {
        try {
            return viaCepClient.searchAddress(cep);
        } catch (IllegalArgumentException ex) {
            throw new CepInvalidoException(ex.getMessage());
        } catch (IllegalStateException | RestClientException ex) {
            throw new CepInvalidoException("Não foi possível validar o CEP no momento. Tente novamente.");
        }
    }
}