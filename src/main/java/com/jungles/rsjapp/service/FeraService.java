package com.jungles.rsjapp.service;

import com.jungles.rsjapp.domain.Fera;
import java.util.List;
import java.util.Optional;

/**
 * Service Interface for managing {@link Fera}.
 */
public interface FeraService {
    /**
     * Save a fera.
     *
     * @param fera the entity to save.
     * @return the persisted entity.
     */
    Fera save(Fera fera);

    /**
     * Updates a fera.
     *
     * @param fera the entity to update.
     * @return the persisted entity.
     */
    Fera update(Fera fera);

    /**
     * Partially updates a fera.
     *
     * @param fera the entity to update partially.
     * @return the persisted entity.
     */
    Optional<Fera> partialUpdate(Fera fera);

    /**
     * Get all the feras.
     *
     * @return the list of entities.
     */
    List<Fera> findAll();

    /**
     * Get the "id" fera.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<Fera> findOne(Long id);

    /**
     * Delete the "id" fera.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
