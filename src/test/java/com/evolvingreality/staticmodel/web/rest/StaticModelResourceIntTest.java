package com.evolvingreality.staticmodel.web.rest;

import com.evolvingreality.staticmodel.StaticmodelApp;
import com.evolvingreality.staticmodel.domain.StaticModel;
import com.evolvingreality.staticmodel.repository.StaticModelRepository;
import com.evolvingreality.staticmodel.service.StaticModelService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import static org.hamcrest.Matchers.hasItem;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.IntegrationTest;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;


/**
 * Test class for the StaticModelResource REST controller.
 *
 * @see StaticModelResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StaticmodelApp.class)
@WebAppConfiguration
@IntegrationTest
public class StaticModelResourceIntTest {


    private static final Integer DEFAULT_ORDINAL = 1;
    private static final Integer UPDATED_ORDINAL = 2;
    private static final String DEFAULT_STATIC_KEY = "AA";
    private static final String UPDATED_STATIC_KEY = "BB";

    @Inject
    private StaticModelRepository staticModelRepository;

    @Inject
    private StaticModelService staticModelService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStaticModelMockMvc;

    private StaticModel staticModel;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StaticModelResource staticModelResource = new StaticModelResource(staticModelService);

        this.restStaticModelMockMvc = MockMvcBuilders.standaloneSetup(staticModelResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        staticModel = new StaticModel();
        staticModel.setOrdinal(DEFAULT_ORDINAL);
        staticModel.setStaticKey(DEFAULT_STATIC_KEY);
    }

    @Test
    @Transactional
    public void createStaticModel() throws Exception {
        int databaseSizeBeforeCreate = staticModelRepository.findAll().size();

        // Create the StaticModel

        restStaticModelMockMvc.perform(post("/api/static-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staticModel)))
                .andExpect(status().isCreated());

        // Validate the StaticModel in the database
        List<StaticModel> staticModels = staticModelRepository.findAll();
        assertThat(staticModels).hasSize(databaseSizeBeforeCreate + 1);
        StaticModel testStaticModel = staticModels.get(staticModels.size() - 1);
        assertThat(testStaticModel.getOrdinal()).isEqualTo(DEFAULT_ORDINAL);
        assertThat(testStaticModel.getStaticKey()).isEqualTo(DEFAULT_STATIC_KEY);
    }

    @Test
    @Transactional
    public void checkStaticKeyIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticModelRepository.findAll().size();
        // set the field null
        staticModel.setStaticKey(null);

        // Create the StaticModel, which fails.

        restStaticModelMockMvc.perform(post("/api/static-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staticModel)))
                .andExpect(status().isBadRequest());

        List<StaticModel> staticModels = staticModelRepository.findAll();
        assertThat(staticModels).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllStaticModels() throws Exception {
        // Initialize the database
        staticModelRepository.saveAndFlush(staticModel);

        // Get all the staticModels
        restStaticModelMockMvc.perform(get("/api/static-models?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(staticModel.getId().intValue())))
                .andExpect(jsonPath("$.[*].ordinal").value(hasItem(DEFAULT_ORDINAL)))
                .andExpect(jsonPath("$.[*].staticKey").value(hasItem(DEFAULT_STATIC_KEY.toString())));
    }

    @Test
    @Transactional
    public void getStaticModel() throws Exception {
        // Initialize the database
        staticModelRepository.saveAndFlush(staticModel);

        // Get the staticModel
        restStaticModelMockMvc.perform(get("/api/static-models/{id}", staticModel.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(staticModel.getId().intValue()))
            .andExpect(jsonPath("$.ordinal").value(DEFAULT_ORDINAL))
            .andExpect(jsonPath("$.staticKey").value(DEFAULT_STATIC_KEY.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStaticModel() throws Exception {
        // Get the staticModel
        restStaticModelMockMvc.perform(get("/api/static-models/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStaticModel() throws Exception {
        // Initialize the database
        staticModelService.save(staticModel);

        int databaseSizeBeforeUpdate = staticModelRepository.findAll().size();

        // Update the staticModel
        StaticModel updatedStaticModel = new StaticModel();
        updatedStaticModel.setId(staticModel.getId());
        updatedStaticModel.setOrdinal(UPDATED_ORDINAL);
        updatedStaticModel.setStaticKey(UPDATED_STATIC_KEY);

        restStaticModelMockMvc.perform(put("/api/static-models")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStaticModel)))
                .andExpect(status().isOk());

        // Validate the StaticModel in the database
        List<StaticModel> staticModels = staticModelRepository.findAll();
        assertThat(staticModels).hasSize(databaseSizeBeforeUpdate);
        StaticModel testStaticModel = staticModels.get(staticModels.size() - 1);
        assertThat(testStaticModel.getOrdinal()).isEqualTo(UPDATED_ORDINAL);
        assertThat(testStaticModel.getStaticKey()).isEqualTo(UPDATED_STATIC_KEY);
    }

    @Test
    @Transactional
    public void deleteStaticModel() throws Exception {
        // Initialize the database
        staticModelService.save(staticModel);

        int databaseSizeBeforeDelete = staticModelRepository.findAll().size();

        // Get the staticModel
        restStaticModelMockMvc.perform(delete("/api/static-models/{id}", staticModel.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StaticModel> staticModels = staticModelRepository.findAll();
        assertThat(staticModels).hasSize(databaseSizeBeforeDelete - 1);
    }
}
