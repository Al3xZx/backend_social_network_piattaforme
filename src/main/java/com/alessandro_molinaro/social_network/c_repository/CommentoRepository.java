package com.alessandro_molinaro.social_network.c_repository;

import com.alessandro_molinaro.social_network.d_entity.Commento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Long> {
}
