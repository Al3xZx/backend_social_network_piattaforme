package com.alessandro_molinaro.social_network.repository;

import com.alessandro_molinaro.social_network.entity.Commento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CommentoRepository extends JpaRepository<Commento, Long> {}
