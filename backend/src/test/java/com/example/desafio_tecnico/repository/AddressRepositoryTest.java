package com.example.desafio_tecnico.repository;

import com.example.desafio_tecnico.model.entity.Address;
import com.example.desafio_tecnico.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class AddressRepositoryTest {

    @Autowired
    private AddressRepository addressRepository;

    @Autowired
    private UserRepository userRepository;

    @Test
    void given_validAddressWithAssociatedUser_when_save_then_persistsWithUserLinked() {
        User user = userRepository.save(new User("João", "joao@email.com", "11888888888"));
        Address address = new Address("01000-000", "Rua Alegre", "100", "SP", "São Paulo", "Centro", user);

        Address salvo = addressRepository.save(address);

        assertThat(salvo.getId()).isNotNull();
        assertThat(addressRepository.findById(salvo.getId())).isPresent();
        assertThat(addressRepository.findById(salvo.getId()).get().getUser().getId()).isEqualTo(user.getId());
    }
}
