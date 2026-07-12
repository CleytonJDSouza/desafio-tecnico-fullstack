package com.example.desafio_tecnico.dto;

import java.util.ArrayList;
import java.util.List;

public class UserResponseDTO {

    private Long id;
    private String nome;
    private String email;
    private String telefone;
    private List<AddressResponseDTO> enderecos = new ArrayList<>();

    public UserResponseDTO() {
    }

    public UserResponseDTO(Long id, String nome, String email, String telefone, List<AddressResponseDTO> enderecos) {
        this.id = id;
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.enderecos = enderecos;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public List<AddressResponseDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<AddressResponseDTO> enderecos) {
        this.enderecos = enderecos;
    }
}
