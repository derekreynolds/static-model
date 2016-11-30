package com.evolvingreality.staticmodel.service;

import com.evolvingreality.staticmodel.domain.StaticModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Service Interface for managing StaticModel.
 */
public interface StaticModelService {

    /**
     * Save a staticModel.
     * 
     * @param staticModel the entity to save
     * @return the persisted entity
     */
    StaticModel save(StaticModel staticModel);

    /**
     *  Get all the staticModels.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StaticModel> findAll(Pageable pageable);

    /**
     *  Get the "id" staticModel.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    StaticModel findOne(Long id);

    /**
     *  Delete the "id" staticModel.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
}
