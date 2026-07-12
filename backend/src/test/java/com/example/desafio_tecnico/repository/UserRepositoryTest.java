package com.example.desafio_tecnico.repository;

import com.example.desafio_tecnico.model.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    void given_validUser_when_saveAndFindById_then_returnsUserWithCorrectData() {
        User user = new User("Maria", "maria@email.com", "11999999999");

        User salvo = userRepository.save(user);

        assertThat(salvo.getId()).isNotNull();
        assertThat(userRepository.findById(salvo.getId())).isPresent();
        assertThat(userRepository.findById(salvo.getId()).get().getNome()).isEqualTo("Maria");
    }
}
