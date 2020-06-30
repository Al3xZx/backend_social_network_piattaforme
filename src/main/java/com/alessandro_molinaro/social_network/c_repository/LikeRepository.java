package com.alessandro_molinaro.social_network.c_repository;

import com.alessandro_molinaro.social_network.d_entity.Like;
import com.alessandro_molinaro.social_network.d_entity.Post;
import com.alessandro_molinaro.social_network.d_entity.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {
}
