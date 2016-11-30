package com.evolvingreality.staticmodel.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.evolvingreality.staticmodel.domain.StaticModel;
import com.evolvingreality.staticmodel.service.StaticModelService;
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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing StaticModel.
 */
@RestController
@RequestMapping("/api")
public class StaticModelResource {

    private final Logger log = LoggerFactory.getLogger(StaticModelResource.class);        
    
    private final StaticModelService staticModelService;
    
    @Autowired
    public StaticModelResource(StaticModelService staticModelService) {
    	this.staticModelService = staticModelService;
    }
    
    /**
     * POST  /static-models : Create a new staticModel.
     *
     * @param staticModel the staticModel to create
     * @return the ResponseEntity with status 201 (Created) and with body the new staticModel, or with status 400 (Bad Request) if the staticModel has already an ID
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/static-models",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaticModel> createStaticModel(@Valid @RequestBody StaticModel staticModel) throws URISyntaxException {
        log.debug("REST request to save StaticModel : {}", staticModel);
        if (staticModel.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("staticModel", "idexists", "A new staticModel cannot already have an ID")).body(null);
        }
        StaticModel result = staticModelService.save(staticModel);
        return ResponseEntity.created(new URI("/api/static-models/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("staticModel", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /static-models : Updates an existing staticModel.
     *
     * @param staticModel the staticModel to update
     * @return the ResponseEntity with status 200 (OK) and with body the updated staticModel,
     * or with status 400 (Bad Request) if the staticModel is not valid,
     * or with status 500 (Internal Server Error) if the staticModel couldnt be updated
     * @throws URISyntaxException if the Location URI syntax is incorrect
     */
    @RequestMapping(value = "/static-models",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaticModel> updateStaticModel(@Valid @RequestBody StaticModel staticModel) throws URISyntaxException {
        log.debug("REST request to update StaticModel : {}", staticModel);
        if (staticModel.getId() == null) {
            return createStaticModel(staticModel);
        }
        StaticModel result = staticModelService.save(staticModel);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("staticModel", staticModel.getId().toString()))
            .body(result);
    }

    /**
     * GET  /static-models : get all the staticModels.
     *
     * @param pageable the pagination information
     * @return the ResponseEntity with status 200 (OK) and the list of staticModels in body
     * @throws URISyntaxException if there is an error to generate the pagination HTTP headers
     */
    @RequestMapping(value = "/static-models",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<StaticModel>> getAllStaticModels(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of StaticModels");
        Page<StaticModel> page = staticModelService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/static-models");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /static-models/:id : get the "id" staticModel.
     *
     * @param id the id of the staticModel to retrieve
     * @return the ResponseEntity with status 200 (OK) and with body the staticModel, or with status 404 (Not Found)
     */
    @RequestMapping(value = "/static-models/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<StaticModel> getStaticModel(@PathVariable Long id) {
        log.debug("REST request to get StaticModel : {}", id);
        StaticModel staticModel = staticModelService.findOne(id);
        return Optional.ofNullable(staticModel)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /static-models/:id : delete the "id" staticModel.
     *
     * @param id the id of the staticModel to delete
     * @return the ResponseEntity with status 200 (OK)
     */
    @RequestMapping(value = "/static-models/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteStaticModel(@PathVariable Long id) {
        log.debug("REST request to delete StaticModel : {}", id);
        staticModelService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("staticModel", id.toString())).build();
    }

}
