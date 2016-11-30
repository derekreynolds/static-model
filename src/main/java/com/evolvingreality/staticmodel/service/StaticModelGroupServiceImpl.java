package com.evolvingreality.staticmodel.service;

import com.evolvingreality.staticmodel.domain.StaticModel;
import com.evolvingreality.staticmodel.domain.StaticModelGroup;
import com.evolvingreality.staticmodel.domain.util.SelectOption;
import com.evolvingreality.staticmodel.repository.StaticModelGroupRepository;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Service Implementation for managing StaticModelGroup.
 */
@Service
@Transactional(readOnly = true)
public class StaticModelGroupServiceImpl implements StaticModelGroupService {

    private final Logger log = LoggerFactory.getLogger(StaticModelGroupServiceImpl.class);
    
    private final StaticModelGroupRepository staticModelGroupRepository;
    
    @Autowired
    public StaticModelGroupServiceImpl(StaticModelGroupRepository staticModelGroupRepository) {
    	this.staticModelGroupRepository = staticModelGroupRepository;
    }
    
    /**
     * Save a staticModelGroup.
     * 
     * @param staticModelGroup the entity to save
     * @return the persisted entity
     */
    @Transactional(readOnly = false)
    @Override
    public StaticModelGroup save(StaticModelGroup staticModelGroup) {
        log.debug("Request to save StaticModelGroup : {}", staticModelGroup);
        StaticModelGroup result = staticModelGroupRepository.save(staticModelGroup);
        return result;
    }

    /**
     *  Get all the staticModelGroups.
     *  
     *  @param pageable the pagination information
     *  @return the list of entities
     */
    @Override
    public Page<StaticModelGroup> findAll(Pageable pageable) {
        log.debug("Request to get all StaticModelGroups");
        Page<StaticModelGroup> result = staticModelGroupRepository.findAll(pageable); 
        return result;
    }

    /**
     *  Get one staticModelGroup by id.
     *
     *  @param id the id of the entity
     *  @return the entity
     */
    @Override 
    public StaticModelGroup findOne(Long id) {
        log.debug("Request to get StaticModelGroup : {}", id);
        StaticModelGroup staticModelGroup = staticModelGroupRepository.findOne(id);
        return staticModelGroup;
    }

    @Override
	public List<SelectOption> findStaticModels(String applicationName, String groupName, String language) {
    	
    	log.debug("Request to get StaticModelGroup : {}, {}, {}", applicationName, groupName, language);
    	
    	List<SelectOption> selectOptions = new ArrayList<>();
    	
    	Optional<StaticModelGroup> staticModelGroup = staticModelGroupRepository
    					.findByApplicationNameAndGroupNameAndStaticModels_Locale(applicationName, groupName, language);    	    	
    	
    	if(staticModelGroup.isPresent()) {    		
    		for(StaticModel staticModel: staticModelGroup.get().getStaticModels())
    			selectOptions.add(new SelectOption(staticModel.getId(), groupName, staticModel.getStaticKey(), staticModel.getModelText()));   		
    	} 
    	
    	return selectOptions;
	}

	/**
     *  Delete the  staticModelGroup by id.
     *  
     *  @param id the id of the entity
     */
    @Transactional(readOnly = false)
    @Override
    public void delete(Long id) {
        log.debug("Request to delete StaticModelGroup : {}", id);
        staticModelGroupRepository.delete(id);
    }
}
