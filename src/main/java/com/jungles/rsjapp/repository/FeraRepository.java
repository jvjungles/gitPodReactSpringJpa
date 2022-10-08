package com.jungles.rsjapp.repository;

import com.jungles.rsjapp.domain.Fera;
import org.springframework.data.jpa.repository.*;
import org.springframework.stereotype.Repository;

/**
 * Spring Data JPA repository for the Fera entity.
 */
@SuppressWarnings("unused")
@Repository
public interface FeraRepository extends JpaRepository<Fera, Long> {}
