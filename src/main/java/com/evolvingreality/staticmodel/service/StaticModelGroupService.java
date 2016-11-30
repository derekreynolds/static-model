package com.evolvingreality.staticmodel.service;

import com.evolvingreality.staticmodel.domain.StaticModelGroup;
import com.evolvingreality.staticmodel.domain.util.SelectOption;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;


/**
 * Service Interface for managing StaticModelGroup.
 */
public interface StaticModelGroupService {

    /**
     * Save a staticModelGroup.
     * 
     * @param staticModelGroup the entity to save
     * @return the persisted entity
     */
    StaticModelGroup save(StaticModelGroup staticModelGroup);

    /**
     *  Get all the staticModelGroups.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    Page<StaticModelGroup> findAll(Pageable pageable);

    /**
     *  Get the "id" staticModelGroup.
     *  
     *  @param id the id of the entity
     *  @return the entity
     */
    StaticModelGroup findOne(Long id);

    /**
     *  Delete the "id" staticModelGroup.
     *  
     *  @param id the id of the entity
     */
    void delete(Long id);
    
    /**
     * Get the {@link StaticModelGroup} for the group name and language. 
     * @param applicationName
     * @param groupName
     * @param language
     * @return
     */
    List<SelectOption> findStaticModels(String applicationName, String groupName, String language);
}
