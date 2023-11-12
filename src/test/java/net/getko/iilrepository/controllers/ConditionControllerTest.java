package net.getko.iilrepository.controllers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import net.getko.iilrepository.TestingConfiguration;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Condition;
import net.getko.iilrepository.models.domain.ConditionType;
import net.getko.iilrepository.models.dto.ConditionDto;
import net.getko.iilrepository.services.ConditionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = ConditionController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(TestingConfiguration.class)
class ConditionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConditionService conditionService;

    @Autowired
    private ObjectMapper objectMapper;

    @Qualifier("conditionDomainToDtoMapper")
    @Autowired
    public DomainDtoMapper conditionDomainToDtoMapper;

    @InjectMocks
    private ConditionController conditionController;

    private List<Condition> conditionList;

    private Condition newCondition;

    private Condition existingCondition;

    @BeforeEach
    void setUp() {
        this.conditionList = new ArrayList<>();
        for(int i=0; i<10 ; i++) {
            Condition condition = new Condition();
            condition.setId(UUID.randomUUID());
            condition.setName("test condition "+i);
            if (i%3 == 0)
                condition.setType(ConditionType.TIME);
            else if (i%3 == 1)
                condition.setType(ConditionType.IIL_STATE);
            else
                condition.setType(ConditionType.IIL_VARIABLE);
            this.conditionList.add(condition);
        }

        this.newCondition = new Condition();
        this.newCondition.setId(UUID.randomUUID());
        this.newCondition.setName("new condition");

        this.existingCondition = new Condition();
        this.existingCondition.setId(UUID.randomUUID());
        this.existingCondition.setName("existing condition");
    }


    /**
     * Test the API returns a list of all conditions
     */
    @Test
    void testGetConditions() throws Exception {
        doReturn(this.conditionList).when(this.conditionService).findAll();

        MvcResult mvcResult = this.mockMvc.perform(get("/api/conditions"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ConditionDto[] result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ConditionDto[].class);
        assertEquals(result.length, this.conditionList.size());
    }

    /**
     * Test the API returns a list of conditions by type
     */
    @Test
    void testGetConditionsByType() throws Exception {
        List<Condition> filteredByType = this.conditionList.stream().filter(condition -> condition.getType().equals(ConditionType.IIL_STATE)).collect(Collectors.toList());;
        doReturn(filteredByType).when(this.conditionService).findAllByType(ConditionType.IIL_STATE);

        String targetType = "IIL_STATE";
        MvcResult mvcResult = this.mockMvc.perform(get("/api/conditions/type/" + targetType.toString()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ConditionDto[] result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ConditionDto[].class);
        assertEquals(result.length, 3);
    }

    /**
     * Test the API returns a single condition
     */
    @Test
    void testGetCondition() throws Exception{
        doReturn(this.existingCondition).when(this.conditionService).findById(this.existingCondition.getId());

        MvcResult mvcResult = this.mockMvc.perform(get("/api/conditions/" + this.existingCondition.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType("application/json"))
                .andReturn();

        ConditionDto result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ConditionDto.class);
        assertEquals(result.getId(), this.existingCondition.getId());
    }

    /**
     * Test the API can create a condition
     */
    @Test
    void testCreateCondition() throws Exception {
        doReturn(this.newCondition).when(this.conditionService).save(this.newCondition);

        String contentStr = this.objectMapper.writeValueAsString(this.conditionDomainToDtoMapper.convertTo(this.newCondition, ConditionDto.class));
        MvcResult mvcResult = this.mockMvc.perform(post("/api/conditions/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(contentStr))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        ConditionDto result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ConditionDto.class);
        assertEquals(result.getId(), this.newCondition.getId());
    }

    /**
     * Test the API can delete a condition
     */
    @Test
    void testDeleteCondition() throws Exception{
        doReturn(this.existingCondition).when(this.conditionService).findById(this.existingCondition.getId());

        MvcResult mvcResult = this.mockMvc.perform(delete("/api/conditions/" + this.existingCondition.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * Test deletion of a non-existing condition
     */
    @Test
    void testDeletionNotFound() throws Exception{
        doReturn(null).when(this.conditionService).findById(this.existingCondition.getId());

        this.mockMvc.perform(delete("/api/conditions/" + this.existingCondition.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound());
    }

    /**
     * Test the API can update a condition
     */
    @Test
    void testUpdateCondition() throws Exception{
        doReturn(this.existingCondition).when(this.conditionService).update(this.existingCondition);

        MvcResult mvcResult = this.mockMvc.perform(put("/api/conditions/" + this.existingCondition.getId())
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(this.objectMapper.writeValueAsString(this.conditionDomainToDtoMapper.convertTo(this.existingCondition, ConditionDto.class))))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();

        ConditionDto result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ConditionDto.class);
        assertEquals(result.getId(), this.existingCondition.getId());
        assertEquals(result.getName(), this.existingCondition.getName());
    }
}
