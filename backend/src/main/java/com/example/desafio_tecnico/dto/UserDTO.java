package com.example.desafio_tecnico.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import java.util.List;

public class UserDTO {

    @NotBlank(message = "nome não pode ser vazio")
    private String nome;

    @NotBlank(message = "email não pode ser vazio")
    @Email(message = "email deve ser um endereço de e-mail válido")
    private String email;

    @NotBlank(message = "telefone não pode ser vazio")
    private String telefone;

    @Valid
    @NotEmpty(message = "usuário deve ter ao menos um endereço")
    private List<AddressDTO> enderecos;

    public UserDTO() {
    }

    public UserDTO(String nome, String email, String telefone, List<AddressDTO> enderecos) {
        this.nome = nome;
        this.email = email;
        this.telefone = telefone;
        this.enderecos = enderecos;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<AddressDTO> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<AddressDTO> enderecos) {
        this.enderecos = enderecos;
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
}
