package com.alessandro_molinaro.social_network.c_repository;

import com.alessandro_molinaro.social_network.d_entity.Post;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import org.springframework.boot.autoconfigure.data.web.SpringDataWebProperties;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UtenteRepository extends JpaRepository<Utente, Long> {

    boolean existsByEmail(String email);

    @Query(value = "SELECT * FROM utente u WHERE u.nome like %?1% OR u.cognome like %?1%", nativeQuery = true)
    List<Utente> selAllByNomeContainingOrCognomeContaining(String s1, Pageable pageable);

    List<Utente> findAllByNomeContaining(String nome);

    List<Utente> findAllByCognomeContaining(String cognome);

    List<Utente> findAllByNomeAndCognome(String nome, String cognome);

    Utente findByEmail(String email);

    @Query(value = "SELECT id_ricevente FROM richieste_amicizia WHERE id_richiedente = ?1 AND id_ricevente = ?2", nativeQuery = true)
    Long richiestaAmicizia(Long richiedente, Long ricevente);

}
