package com.evolvingreality.staticmodel.web.rest;

import com.evolvingreality.staticmodel.StaticmodelApp;
import com.evolvingreality.staticmodel.domain.StaticModelGroup;
import com.evolvingreality.staticmodel.repository.StaticModelGroupRepository;
import com.evolvingreality.staticmodel.service.StaticModelGroupService;

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
 * Test class for the StaticModelGroupResource REST controller.
 *
 * @see StaticModelGroupResource
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = StaticmodelApp.class)
@WebAppConfiguration
@IntegrationTest
public class StaticModelGroupResourceIntTest {

    private static final String DEFAULT_GROUP_NAME = "AAAAA";
    private static final String UPDATED_GROUP_NAME = "BBBBB";

    @Inject
    private StaticModelGroupRepository staticModelGroupRepository;

    @Inject
    private StaticModelGroupService staticModelGroupService;

    @Inject
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Inject
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    private MockMvc restStaticModelGroupMockMvc;

    private StaticModelGroup staticModelGroup;

    @PostConstruct
    public void setup() {
        MockitoAnnotations.initMocks(this);
        StaticModelGroupResource staticModelGroupResource = new StaticModelGroupResource(staticModelGroupService);
        this.restStaticModelGroupMockMvc = MockMvcBuilders.standaloneSetup(staticModelGroupResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setMessageConverters(jacksonMessageConverter).build();
    }

    @Before
    public void initTest() {
        staticModelGroup = new StaticModelGroup();
        staticModelGroup.setGroupName(DEFAULT_GROUP_NAME);
    }

    @Test
    @Transactional
    public void createStaticModelGroup() throws Exception {
        int databaseSizeBeforeCreate = staticModelGroupRepository.findAll().size();

        // Create the StaticModelGroup

        restStaticModelGroupMockMvc.perform(post("/api/static-model-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(staticModelGroup)))
                .andExpect(status().isCreated());

        // Validate the StaticModelGroup in the database
        List<StaticModelGroup> staticModelGroups = staticModelGroupRepository.findAll();
        assertThat(staticModelGroups).hasSize(databaseSizeBeforeCreate + 1);
        StaticModelGroup testStaticModelGroup = staticModelGroups.get(staticModelGroups.size() - 1);
        assertThat(testStaticModelGroup.getGroupName()).isEqualTo(DEFAULT_GROUP_NAME);
    }

    @Test
    @Transactional
    public void getAllStaticModelGroups() throws Exception {
        // Initialize the database
        staticModelGroupRepository.saveAndFlush(staticModelGroup);

        // Get all the staticModelGroups
        restStaticModelGroupMockMvc.perform(get("/api/static-model-groups?sort=id,desc"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.[*].id").value(hasItem(staticModelGroup.getId().intValue())))
                .andExpect(jsonPath("$.[*].groupName").value(hasItem(DEFAULT_GROUP_NAME.toString())));
    }

    @Test
    @Transactional
    public void getStaticModelGroup() throws Exception {
        // Initialize the database
        staticModelGroupRepository.saveAndFlush(staticModelGroup);

        // Get the staticModelGroup
        restStaticModelGroupMockMvc.perform(get("/api/static-model-groups/{id}", staticModelGroup.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.id").value(staticModelGroup.getId().intValue()))
            .andExpect(jsonPath("$.groupName").value(DEFAULT_GROUP_NAME.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingStaticModelGroup() throws Exception {
        // Get the staticModelGroup
        restStaticModelGroupMockMvc.perform(get("/api/static-model-groups/{id}", Long.MAX_VALUE))
                .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateStaticModelGroup() throws Exception {
        // Initialize the database
        staticModelGroupService.save(staticModelGroup);

        int databaseSizeBeforeUpdate = staticModelGroupRepository.findAll().size();

        // Update the staticModelGroup
        StaticModelGroup updatedStaticModelGroup = new StaticModelGroup();
        updatedStaticModelGroup.setId(staticModelGroup.getId());
        updatedStaticModelGroup.setGroupName(UPDATED_GROUP_NAME);

        restStaticModelGroupMockMvc.perform(put("/api/static-model-groups")
                .contentType(TestUtil.APPLICATION_JSON_UTF8)
                .content(TestUtil.convertObjectToJsonBytes(updatedStaticModelGroup)))
                .andExpect(status().isOk());

        // Validate the StaticModelGroup in the database
        List<StaticModelGroup> staticModelGroups = staticModelGroupRepository.findAll();
        assertThat(staticModelGroups).hasSize(databaseSizeBeforeUpdate);
        StaticModelGroup testStaticModelGroup = staticModelGroups.get(staticModelGroups.size() - 1);
        assertThat(testStaticModelGroup.getGroupName()).isEqualTo(UPDATED_GROUP_NAME);
    }

    @Test
    @Transactional
    public void deleteStaticModelGroup() throws Exception {
        // Initialize the database
        staticModelGroupService.save(staticModelGroup);

        int databaseSizeBeforeDelete = staticModelGroupRepository.findAll().size();

        // Get the staticModelGroup
        restStaticModelGroupMockMvc.perform(delete("/api/static-model-groups/{id}", staticModelGroup.getId())
                .accept(TestUtil.APPLICATION_JSON_UTF8))
                .andExpect(status().isOk());

        // Validate the database is empty
        List<StaticModelGroup> staticModelGroups = staticModelGroupRepository.findAll();
        assertThat(staticModelGroups).hasSize(databaseSizeBeforeDelete - 1);
    }
}
