package com.example.desafio_tecnico.service;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Service
public class ViaCepClient {

    private static final String VIA_CEP_URL = "https://viacep.com.br/ws/{cep}/json";

    private final RestTemplate restTemplate;

    public ViaCepClient(RestTemplateBuilder restTemplateBuilder) {
        this.restTemplate = restTemplateBuilder.build();
    }

    public ViaCepResponse searchAddress(String cep) {
        String normalizedCep = Optional.ofNullable(cep)
                .map(value -> value.replaceAll("\\D", ""))
                .orElse("");

        if (normalizedCep.length() != 8) {
            throw new IllegalArgumentException("CEP inválido");
        }

        ResponseEntity<ViaCepResponse> response = restTemplate.getForEntity(VIA_CEP_URL, ViaCepResponse.class, normalizedCep);

        if (!response.getStatusCode().is2xxSuccessful() || response.getBody() == null) {
            throw new IllegalStateException("Falha ao consultar o serviço ViaCEP");
        }

        ViaCepResponse body = response.getBody();
        if (Boolean.TRUE.equals(body.getErro())) {
            throw new IllegalArgumentException("CEP não encontrado");
        }

        return body;
    }

    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class ViaCepResponse {
        private String cep;
        private String logradouro;
        private String complemento;
        private String bairro;
        private String localidade;
        private String uf;
        private String ibge;
        private String gia;
        private String ddd;
        private String siafi;
        private Boolean erro;

        public String getCep() {
            return cep;
        }

        public void setCep(String cep) {
            this.cep = cep;
        }

        public String getLogradouro() {
            return logradouro;
        }

        public void setLogradouro(String logradouro) {
            this.logradouro = logradouro;
        }

        public String getComplemento() {
            return complemento;
        }

        public void setComplemento(String complemento) {
            this.complemento = complemento;
        }

        public String getBairro() {
            return bairro;
        }

        public void setBairro(String bairro) {
            this.bairro = bairro;
        }

        public String getLocalidade() {
            return localidade;
        }

        public void setLocalidade(String localidade) {
            this.localidade = localidade;
        }

        public String getUf() {
            return uf;
        }

        public void setUf(String uf) {
            this.uf = uf;
        }

        public String getIbge() {
            return ibge;
        }

        public void setIbge(String ibge) {
            this.ibge = ibge;
        }

        public String getGia() {
            return gia;
        }

        public void setGia(String gia) {
            this.gia = gia;
        }

        public String getDdd() {
            return ddd;
        }

        public void setDdd(String ddd) {
            this.ddd = ddd;
        }

        public String getSiafi() {
            return siafi;
        }

        public void setSiafi(String siafi) {
            this.siafi = siafi;
        }

        public Boolean getErro() {
            return erro;
        }

        public void setErro(Boolean erro) {
            this.erro = erro;
        }
    }
}
