package com.example.desafio_tecnico.controller;

import com.example.desafio_tecnico.dto.CepConsultaResponseDTO;
import com.example.desafio_tecnico.service.CepValidationService;
import com.example.desafio_tecnico.service.ViaCepClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cep")
public class CepController {

    private final CepValidationService cepValidationService;

    public CepController(CepValidationService cepValidationService) {
        this.cepValidationService = cepValidationService;
    }

    @GetMapping("/{cep}")
    public ResponseEntity<CepConsultaResponseDTO> consultar(@PathVariable String cep) {
        ViaCepClient.ViaCepResponse resultado = cepValidationService.validar(cep);

        CepConsultaResponseDTO response = new CepConsultaResponseDTO(
                resultado.getCep(),
                resultado.getLogradouro(),
                resultado.getBairro(),
                resultado.getLocalidade(),
                resultado.getUf()
        );

        return ResponseEntity.ok(response);
    }
}