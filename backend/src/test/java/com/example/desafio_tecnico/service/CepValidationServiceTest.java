package com.example.desafio_tecnico.service;

import com.example.desafio_tecnico.exception.CepInvalidoException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestClientException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CepValidationServiceTest {

    @Mock
    private ViaCepClient viaCepClient;

    @InjectMocks
    private CepValidationService cepValidationService;

    @Test
    void given_validCep_when_validar_then_returnsViaCepResponseWithAddressData() {
        ViaCepClient.ViaCepResponse response = new ViaCepClient.ViaCepResponse();
        response.setCep("01000-000");
        response.setLogradouro("Rua das Flores");
        response.setBairro("Centro");
        response.setLocalidade("São Paulo");
        response.setUf("SP");
        response.setErro(false);

        when(viaCepClient.searchAddress("01000000")).thenReturn(response);

        ViaCepClient.ViaCepResponse resultado = cepValidationService.validar("01000000");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getLogradouro()).isEqualTo("Rua das Flores");
        assertThat(resultado.getLocalidade()).isEqualTo("São Paulo");
    }

    @Test
    void given_invalidCepFormat_when_validar_then_throwsCepInvalidoException() {
        when(viaCepClient.searchAddress("123")).thenThrow(new IllegalArgumentException("CEP inválido"));

        assertThatThrownBy(() -> cepValidationService.validar("123"))
                .isInstanceOf(CepInvalidoException.class)
                .hasMessage("CEP inválido");
    }

    @Test
    void given_connectionErrorWithViaCep_when_validar_then_throwsCepInvalidoException() {
        when(viaCepClient.searchAddress("01000000")).thenThrow(new RestClientException("timeout"));

        assertThatThrownBy(() -> cepValidationService.validar("01000000"))
                .isInstanceOf(CepInvalidoException.class)
                .hasMessage("Não foi possível validar o CEP no momento. Tente novamente.");
    }
}
