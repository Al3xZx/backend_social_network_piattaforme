package com.alessandro_molinaro.social_network.c_repository;

import com.alessandro_molinaro.social_network.d_entity.Post;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    List<Post> findAllByUtente(Utente utente, Pageable pageable);

    //lista post di tutti gli amici di un determinato utente
    @Query(value = "SELECT p.id, p.data_creazione, p.contenuto_testuale, p.id_utente " +
                   "FROM post AS p, amici AS a " +
                   "WHERE a.id_utente_a = ?1 AND p.id_utente = a.id_utente_b", nativeQuery = true)
    List<Post> selAllPostDiAmici(Long userId, Pageable pageable);

}
