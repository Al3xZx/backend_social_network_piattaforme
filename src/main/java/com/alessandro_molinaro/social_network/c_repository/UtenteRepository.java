package com.alessandro_molinaro.social_network.c_repository;

import com.alessandro_molinaro.social_network.d_entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    boolean existsByEmail(String email);

    List<Utente> findAllByNome(String nome);

    List<Utente> findAllByCognome(String cognome);

    List<Utente> findAllByNomeAndCognome(String nome, String cognome);

    Utente findByEmail(String email);

}
