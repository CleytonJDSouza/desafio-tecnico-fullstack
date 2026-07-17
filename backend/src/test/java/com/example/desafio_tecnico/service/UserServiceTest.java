package com.example.desafio_tecnico.service;

import com.example.desafio_tecnico.dto.AddressDTO;
import com.example.desafio_tecnico.dto.UserDTO;
import com.example.desafio_tecnico.dto.UserResponseDTO;
import com.example.desafio_tecnico.exception.CepInvalidoException;
import com.example.desafio_tecnico.exception.ResourceNotFoundException;
import com.example.desafio_tecnico.model.entity.Address;
import com.example.desafio_tecnico.model.entity.User;
import com.example.desafio_tecnico.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private CepValidationService cepValidationService;

    @InjectMocks
    private UserService userService;

    @Test
    void given_noUsers_when_listAll_then_returnsEmptyList() {
        when(userRepository.findAll()).thenReturn(Collections.emptyList());

        List<UserResponseDTO> result = userService.listAll();

        assertThat(result).isNotNull().isEmpty();
    }

    @Test
    void given_usersExist_when_listAll_then_returnsMappedUserResponseDTOList() {
        User user = new User();
        user.setId(1L);
        user.setNome("João Silva");
        user.setEmail("joao@example.com");
        user.setTelefone("11999999999");

        when(userRepository.findAll()).thenReturn(List.of(user));

        List<UserResponseDTO> result = userService.listAll();

        assertThat(result).hasSize(1);
        UserResponseDTO dto = result.get(0);
        assertThat(dto.getId()).isEqualTo(1L);
        assertThat(dto.getNome()).isEqualTo("João Silva");
        assertThat(dto.getEmail()).isEqualTo("joao@example.com");
        assertThat(dto.getTelefone()).isEqualTo("11999999999");
        assertThat(dto.getEnderecos()).isEmpty();
    }

    @Test
    void given_existingId_when_findById_then_returnsUserResponseDTO() {
        User user = new User();
        user.setId(2L);
        user.setNome("Maria Souza");
        user.setEmail("maria@example.com");
        user.setTelefone("21988888888");

        when(userRepository.findById(2L)).thenReturn(Optional.of(user));

        UserResponseDTO result = userService.findById(2L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(2L);
        assertThat(result.getNome()).isEqualTo("Maria Souza");
        assertThat(result.getEmail()).isEqualTo("maria@example.com");
        assertThat(result.getTelefone()).isEqualTo("21988888888");
    }

    @Test
    void given_validAddress_when_create_then_savesUserWithValidatedAddress() {
        AddressDTO addressDTO = new AddressDTO("01000000", "123", null, null, null, null);
        UserDTO userDTO = new UserDTO("Carlos Lima", "carlos@example.com", "21977777777", List.of(addressDTO));

        User savedUser = new User();
        savedUser.setId(4L);
        savedUser.setNome("Carlos Lima");
        savedUser.setEmail("carlos@example.com");
        savedUser.setTelefone("21977777777");
        Address address = new Address();
        address.setId(10L);
        address.setCep("01000000");
        address.setRua("Rua Teste");
        address.setBairro("Bairro Teste");
        address.setCidade("Cidade Teste");
        address.setEstado("SP");
        savedUser.addAddress(address);

        ViaCepClient.ViaCepResponse viaCepResponse = new ViaCepClient.ViaCepResponse();
        viaCepResponse.setLogradouro("Rua Teste");
        viaCepResponse.setBairro("Bairro Teste");
        viaCepResponse.setLocalidade("Cidade Teste");
        viaCepResponse.setUf("SP");

        when(cepValidationService.validar("01000000")).thenReturn(viaCepResponse);
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenReturn(savedUser);

        UserResponseDTO result = userService.create(userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(4L);
        assertThat(result.getNome()).isEqualTo("Carlos Lima");
        assertThat(result.getEmail()).isEqualTo("carlos@example.com");
        assertThat(result.getTelefone()).isEqualTo("21977777777");
        assertThat(result.getEnderecos()).hasSize(1);
        assertThat(result.getEnderecos().get(0).getCep()).isEqualTo("01000000");
        assertThat(result.getEnderecos().get(0).getRua()).isEqualTo("Rua Teste");
    }

    @Test
    void given_invalidCep_when_create_then_propagatesCepInvalidoException() {
        AddressDTO addressDTO = new AddressDTO("00000000", "123", null, null, null, null);
        UserDTO userDTO = new UserDTO("Carlos Lima", "carlos@example.com", "21977777777", List.of(addressDTO));

        when(cepValidationService.validar("00000000")).thenThrow(new CepInvalidoException("CEP inválido"));

        assertThatThrownBy(() -> userService.create(userDTO))
                .isInstanceOf(CepInvalidoException.class)
                .hasMessage("CEP inválido");
    }

    @Test
    void given_existingIdAndUpdatedData_when_update_then_updatesUserData() {
        User existingUser = new User();
        existingUser.setId(5L);
        existingUser.setNome("Ana Clara");
        existingUser.setEmail("ana@example.com");
        existingUser.setTelefone("21966666666");

        AddressDTO addressDTO = new AddressDTO("01000000", "456", null, null, null, null);
        UserDTO userDTO = new UserDTO("Ana Clara Silva", "ana.silva@example.com", "21966666667", List.of(addressDTO));

        ViaCepClient.ViaCepResponse viaCepResponse = new ViaCepClient.ViaCepResponse();
        viaCepResponse.setLogradouro("Rua Atualizada");
        viaCepResponse.setBairro("Bairro Atualizado");
        viaCepResponse.setLocalidade("Cidade Atualizada");
        viaCepResponse.setUf("RJ");

        when(userRepository.findById(5L)).thenReturn(Optional.of(existingUser));
        when(cepValidationService.validar("01000000")).thenReturn(viaCepResponse);
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO result = userService.update(5L, userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(5L);
        assertThat(result.getNome()).isEqualTo("Ana Clara Silva");
        assertThat(result.getEmail()).isEqualTo("ana.silva@example.com");
        assertThat(result.getTelefone()).isEqualTo("21966666667");
        assertThat(result.getEnderecos()).hasSize(1);
        assertThat(result.getEnderecos().get(0).getRua()).isEqualTo("Rua Atualizada");
    }

    @Test
    void given_nonExistingId_when_update_then_throwsResourceNotFoundException() {
        AddressDTO addressDTO = new AddressDTO("01000000", "456", null, null, null, null);
        UserDTO userDTO = new UserDTO("Ana Clara Silva", "ana.silva@example.com", "21966666667", List.of(addressDTO));

        when(userRepository.findById(6L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.update(6L, userDTO))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }

    @Test
    void given_existingUserWithAddresses_when_update_then_replacesOldAddressesWithNewOnes() {
        User existingUser = new User();
        existingUser.setId(7L);
        existingUser.setNome("Bruno");
        existingUser.setEmail("bruno@example.com");
        existingUser.setTelefone("21955555555");

        Address oldAddress = new Address();
        oldAddress.setId(20L);
        oldAddress.setCep("02000000");
        oldAddress.setRua("Rua Velha");
        existingUser.addAddress(oldAddress);

        AddressDTO newAddressDTO = new AddressDTO("01000000", "789", null, null, null, null);
        UserDTO userDTO = new UserDTO("Bruno Silva", "bruno.silva@example.com", "21955555556", List.of(newAddressDTO));

        ViaCepClient.ViaCepResponse viaCepResponse = new ViaCepClient.ViaCepResponse();
        viaCepResponse.setLogradouro("Rua Nova");
        viaCepResponse.setBairro("Bairro Novo");
        viaCepResponse.setLocalidade("Cidade Nova");
        viaCepResponse.setUf("SP");

        when(userRepository.findById(7L)).thenReturn(Optional.of(existingUser));
        when(cepValidationService.validar("01000000")).thenReturn(viaCepResponse);
        when(userRepository.save(org.mockito.ArgumentMatchers.any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        UserResponseDTO result = userService.update(7L, userDTO);

        assertThat(result).isNotNull();
        assertThat(result.getEnderecos()).hasSize(1);
        assertThat(result.getEnderecos().get(0).getRua()).isEqualTo("Rua Nova");
        assertThat(result.getEnderecos().get(0).getCep()).isEqualTo("01000000");
    }

    @Test
    void given_existingId_when_delete_then_removesUser() {
        User existingUser = new User();
        existingUser.setId(8L);
        existingUser.setNome("Daniel");
        existingUser.setEmail("daniel@example.com");
        existingUser.setTelefone("21944444444");

        when(userRepository.findById(8L)).thenReturn(Optional.of(existingUser));

        userService.delete(8L);

        verify(userRepository).delete(existingUser);
    }

    @Test
    void given_nonExistingId_when_delete_then_throwsResourceNotFoundException() {
        when(userRepository.findById(9L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.delete(9L))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessage("Usuário não encontrado");
    }
}
