package com.jungles.rsjapp.service.impl;

import com.jungles.rsjapp.domain.Fera;
import com.jungles.rsjapp.repository.FeraRepository;
import com.jungles.rsjapp.service.FeraService;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Service Implementation for managing {@link Fera}.
 */
@Service
@Transactional
public class FeraServiceImpl implements FeraService {

    private final Logger log = LoggerFactory.getLogger(FeraServiceImpl.class);

    private final FeraRepository feraRepository;

    public FeraServiceImpl(FeraRepository feraRepository) {
        this.feraRepository = feraRepository;
    }

    @Override
    public Fera save(Fera fera) {
        log.debug("Request to save Fera : {}", fera);
        return feraRepository.save(fera);
    }

    @Override
    public Fera update(Fera fera) {
        log.debug("Request to update Fera : {}", fera);
        return feraRepository.save(fera);
    }

    @Override
    public Optional<Fera> partialUpdate(Fera fera) {
        log.debug("Request to partially update Fera : {}", fera);

        return feraRepository
            .findById(fera.getId())
            .map(existingFera -> {
                if (fera.getFeraName() != null) {
                    existingFera.setFeraName(fera.getFeraName());
                }

                return existingFera;
            })
            .map(feraRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Fera> findAll() {
        log.debug("Request to get all Feras");
        return feraRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Fera> findOne(Long id) {
        log.debug("Request to get Fera : {}", id);
        return feraRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Fera : {}", id);
        feraRepository.deleteById(id);
    }
}
