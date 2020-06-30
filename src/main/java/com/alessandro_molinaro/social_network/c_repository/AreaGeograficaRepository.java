package com.alessandro_molinaro.social_network.c_repository;

import com.alessandro_molinaro.social_network.d_entity.AreaGeografica;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AreaGeograficaRepository extends JpaRepository<AreaGeografica, Long> {

    AreaGeografica findByNazioneAndRegioneAndCitta(String nazione, String regione, String citta);
}
