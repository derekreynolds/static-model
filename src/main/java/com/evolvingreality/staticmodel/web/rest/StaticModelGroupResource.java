package com.evolvingreality.staticmodel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evolvingreality.staticmodel.domain.StaticModelGroup;
import com.evolvingreality.staticmodel.domain.util.SelectOption;
import com.evolvingreality.staticmodel.service.StaticModelGroupService;
import com.evolvingreality.staticmodel.web.rest.util.HeaderUtil;
import com.evolvingreality.staticmodel.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * REST controller for managing StaticModelGroup.
 */
@RestController
@RequestMapping("/api")
public class StaticModelGroupResource {

    private final Logger log = LoggerFactory.getLogger(StaticModelGroupResource.class);
        
    private final StaticModelGroupService staticModelGroupService;
    
    @Autowired
    public StaticModelGroupResource(StaticModelGroupService staticModelGroupService) {
    	this.staticModelGroupService = staticModelGroupService;
    }
    
    /**
     * POST  /static-model-groups : Create a new staticModelGroup.
     *
     * @param staticModelGroup the staticModelGroup to create
     * @return the ResponseEntity with status 201 (Created) and with body the new staticModelGroup, or with status 400 (Bad Request) if the staticModelGroup has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/static-model-groups",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaticModelGroup> createStaticModelGroup(@RequestBody StaticModelGroup staticModelGroup) throws URISyntaxException {
        log.debug("REST request to save StaticModelGroup : {}", staticModelGroup);
        if (staticModelGroup.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("staticModelGroup", "idexists", "A new staticModelGroup cannot already have an ID")).body(null);
        }
        StaticModelGroup result = staticModelGroupService.save(staticModelGroup);
        return ResponseEntity.created(new URI("/api/static-model-groups/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("staticModelGroup", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /static-model-groups : Updates an existing staticModelGroup.
     *
     * @param staticModelGroup the staticModelGroup to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated staticModelGroup,
     * or with status 400 (Bad Request) if the staticModelGroup is not valid,
     * or with status 500 (Internal Server Error) if the staticModelGroup couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/static-model-groups",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaticModelGroup> updateStaticModelGroup(@RequestBody StaticModelGroup staticModelGroup) throws URISyntaxException {
        log.debug("REST request to update StaticModelGroup : {}", staticModelGroup);
        if (staticModelGroup.getId() == null) {
            return createStaticModelGroup(staticModelGroup);
        }
        StaticModelGroup result = staticModelGroupService.save(staticModelGroup);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("staticModelGroup", staticModelGroup.getId().toString()))
            .body(result);
    }

    /**
     * GET  /static-model-groups : get all the staticModelGroups.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of staticModelGroups in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/static-model-groups",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StaticModelGroup>> getAllStaticModelGroups(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StaticModelGroups");
        Page<StaticModelGroup> page = staticModelGroupService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/static-model-groups");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /static-model-groups/:id : get the "id" staticModelGroup.
     *
     * @param id the id of the staticModelGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staticModelGroup, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/static-model-groups/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaticModelGroup> getStaticModelGroup(@PathVariable Long id) {
        log.debug("REST request to get StaticModelGroup : {}", id);
        StaticModelGroup staticModelGroup = staticModelGroupService.findOne(id);
        return Optional.ofNullable(staticModelGroup)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }
    
    /**
     * GET  /static-model-groups/:groupName/language/:language : get the "groupName" staticModelGroup.
     *
     * @param id the id of the staticModelGroup to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staticModelGroup, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/static-model-groups/application/{applicationName}/group/{groupName}/language/{language}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<SelectOption>> getStaticModelGroup(@PathVariable String applicationName, @PathVariable String groupName, @PathVariable String language) {
        log.debug("REST request to get StaticModelMap : {}, {}, {}", applicationName, groupName, language);
        
        return new ResponseEntity<>(staticModelGroupService.findStaticModels(applicationName, groupName, language),
                HttpStatus.OK);
    }

    /**
     * DELETE  /static-model-groups/:id : delete the "id" staticModelGroup.
     *
     * @param id the id of the staticModelGroup to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/static-model-groups/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStaticModelGroup(@PathVariable Long id) {
        log.debug("REST request to delete StaticModelGroup : {}", id);
        staticModelGroupService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("staticModelGroup", id.toString())).build();
    }

}
