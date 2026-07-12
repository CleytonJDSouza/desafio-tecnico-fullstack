package com.example.desafio_tecnico.service;

import com.example.desafio_tecnico.dto.AddressDTO;
import com.example.desafio_tecnico.dto.AddressResponseDTO;
import com.example.desafio_tecnico.dto.UserDTO;
import com.example.desafio_tecnico.dto.UserResponseDTO;
import com.example.desafio_tecnico.exception.ResourceNotFoundException;
import com.example.desafio_tecnico.model.entity.Address;
import com.example.desafio_tecnico.model.entity.User;
import com.example.desafio_tecnico.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final CepValidationService cepValidationService;

    public UserService(UserRepository userRepository, CepValidationService cepValidationService) {
        this.userRepository = userRepository;
        this.cepValidationService = cepValidationService;
    }

    public List<UserResponseDTO> listAll() {
        return userRepository.findAll().stream()
                .map(this::toResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO findById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        return toResponseDTO(user);
    }

    public UserResponseDTO create(UserDTO userDTO) {
        User user = new User();
        user.setNome(userDTO.getNome());
        user.setEmail(userDTO.getEmail());
        user.setTelefone(userDTO.getTelefone());

        if (userDTO.getEnderecos() != null) {
            for (AddressDTO addressDTO : userDTO.getEnderecos()) {
                Address address = buildAddressFromDTO(addressDTO);
                user.addAddress(address);
            }
        }

        User savedUser = userRepository.save(user);
        return toResponseDTO(savedUser);
    }

    public UserResponseDTO update(Long id, UserDTO userDTO) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));

        user.setNome(userDTO.getNome());
        user.setEmail(userDTO.getEmail());
        user.setTelefone(userDTO.getTelefone());

        if (userDTO.getEnderecos() != null) {
            user.getEnderecos().clear(); // remove os antigos (orphanRemoval cuida da exclusão no banco)
            for (AddressDTO addressDTO : userDTO.getEnderecos()) {
                Address address = buildAddressFromDTO(addressDTO);
                user.addAddress(address);
            }
        }

        User updatedUser = userRepository.save(user);
        return toResponseDTO(updatedUser);
    }

    public void delete(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Usuário não encontrado"));
        userRepository.delete(user);
    }

    private UserResponseDTO toResponseDTO(User user) {
        List<AddressResponseDTO> addresses = user.getEnderecos().stream()
                .map(this::toAddressResponseDTO)
                .collect(Collectors.toList());

        return new UserResponseDTO(
                user.getId(),
                user.getNome(),
                user.getEmail(),
                user.getTelefone(),
                addresses
        );
    }

    private AddressResponseDTO toAddressResponseDTO(Address address) {
        return new AddressResponseDTO(
                address.getId(),
                address.getCep(),
                address.getRua(),
                address.getNumero(),
                address.getEstado(),
                address.getCidade(),
                address.getBairro()
        );
    }

    private Address buildAddressFromDTO(AddressDTO dto) {
        var viaCepData = cepValidationService.validar(dto.getCep());

        Address address = new Address();
        address.setCep(dto.getCep());
        address.setNumero(dto.getNumero());
        address.setRua(viaCepData.getLogradouro());
        address.setBairro(viaCepData.getBairro());
        address.setCidade(viaCepData.getLocalidade());
        address.setEstado(viaCepData.getUf());

        return address;
    }
}
