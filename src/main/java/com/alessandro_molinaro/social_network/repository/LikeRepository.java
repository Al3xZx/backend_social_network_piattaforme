package com.alessandro_molinaro.social_network.repository;

import com.alessandro_molinaro.social_network.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikeRepository extends JpaRepository<Like, Long> {}
