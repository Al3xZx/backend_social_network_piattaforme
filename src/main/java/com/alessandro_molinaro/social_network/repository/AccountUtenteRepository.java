package com.alessandro_molinaro.social_network.repository;

import com.alessandro_molinaro.social_network.entity.AccountUtente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AccountUtenteRepository extends JpaRepository<AccountUtente, String> {}
