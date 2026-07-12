package com.example.desafio_tecnico.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class AddressDTO {

    @NotBlank(message = "cep não pode ser vazio")
    @Pattern(regexp = "\\d{8}", message = "cep deve conter 8 dígitos numéricos")
    private String cep;
    @NotBlank(message = "numero não pode ser vazio")
    private String numero;
    private String rua;
    private String estado;
    private String cidade;
    private String bairro;

    public AddressDTO() {
    }

    public AddressDTO(String cep, String numero, String rua, String estado, String cidade, String bairro) {
        this.cep = cep;
        this.numero = numero;
        this.rua = rua;
        this.estado = estado;
        this.cidade = cidade;
        this.bairro = bairro;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }
}