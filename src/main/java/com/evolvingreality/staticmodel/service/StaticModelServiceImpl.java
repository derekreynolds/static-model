package com.evolvingreality.staticmodel.service;

import com.evolvingreality.staticmodel.domain.StaticModel;
import com.evolvingreality.staticmodel.repository.StaticModelRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;


/**
 * Service Implementation for managing StaticModel.
 */
@Service
@Transactional(readOnly = true)
public class StaticModelServiceImpl implements StaticModelService{

    private final Logger log = LoggerFactory.getLogger(StaticModelServiceImpl.class);
    
   
    private final StaticModelRepository staticModelRepository;
    
    @Autowired
    public StaticModelServiceImpl(StaticModelRepository staticModelRepository) {
    	this.staticModelRepository = staticModelRepository;
    }
    
    /**
     * Save a staticModel.
     * 
     * @param staticModel the entity to save
     * @return the persisted entity
     */
    @Transactional(readOnly = false)
    @Override
    public StaticModel save(StaticModel staticModel) {
        log.debug("Request to save StaticModel : {}", staticModel);
        StaticModel result = staticModelRepository.save(staticModel);
        return result;
    }

    /**
     *  Get all the staticModels.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<StaticModel> findAll(Pageable pageable) {
        log.debug("Request to get all StaticModels");
        Page<StaticModel> result = staticModelRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one staticModel by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override 
    public StaticModel findOne(Long id) {
        log.debug("Request to get StaticModel : {}", id);
        StaticModel staticModel = staticModelRepository.findOne(id);
        return staticModel;
    }

    /**
     *  Delete the  staticModel by id.
     *  
     *  @param id the id of the entity
     */
    @Transactional(readOnly = false)
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StaticModel : {}", id);
        staticModelRepository.delete(id);
    }
}
