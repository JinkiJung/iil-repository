package net.getko.iilrepository.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.getko.iilrepository.TestingConfiguration;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Action;
import net.getko.iilrepository.models.domain.Actor;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.models.domain.User;
import net.getko.iilrepository.models.dto.IilDto;
import net.getko.iilrepository.services.IilService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = IilController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(TestingConfiguration.class)
public class IilControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IilService iilService;

    @Autowired
    private ObjectMapper objectMapper;

    @Qualifier("iilDomainToDtoMapper")
    @Autowired
    public DomainDtoMapper illDomainToDtoMapper;

    @InjectMocks
    private IilController iilController;

    private List<Iil> iilList;

    private Iil newIil;

    private Iil existingIil;

    private Actor testActor1;
    @BeforeEach
    void setUp() {
        this.testActor1 = new User();
        this.testActor1.setId(UUID.fromString("e91ab6d1-2585-422e-94a7-e538cbf284c3"));
        this.testActor1.setName("test user");

        // Initialise the iils list
        this.iilList = new ArrayList<>();
        for (int i=0 ; i<10 ; i++) {
            Iil iil = new Iil();
            iil.setId(UUID.randomUUID());
            iil.setAct(new Action("Test "+i));
            iil.setActor(this.testActor1);
            this.iilList.add(iil);
        }

        // initialise new iil
        this.newIil = new Iil();
        this.newIil.setId(UUID.randomUUID());
        this.newIil.setAct(new Action("Test new"));

        // initialize existing iil
        this.existingIil = new Iil();
        this.existingIil.setId(UUID.randomUUID());
        this.existingIil.setAct(new Action("Test existing"));

    }

    /**
     * Test the API can return a list of all iils.
     */
    @Test
    void testGetFullIilList() throws Exception {
        doReturn(this.iilList).when(this.iilService).findAll();
        // Perform the MVC request
        MvcResult mvcResult = this.mockMvc.perform(get("/api/iils"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        IilDto[] result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), IilDto[].class);
        assertEquals(10, Arrays.asList(result).size());
    }

    /**
     * Test the API can return an iil for given ID
     */
    @Test
    void testGetIilWithId() throws Exception {
        doReturn(this.existingIil).when(this.iilService).findById(this.existingIil.getId());
        // Perform the MVC request
        MvcResult mvcResult = this.mockMvc.perform(get("/api/iils/{id}", this.existingIil.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        IilDto result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), IilDto.class);
        // convert result to Iil type
        assertNotNull(result);
        assertEquals(this.existingIil.getId(), result.getId());
        assertEquals(this.existingIil.getAct().getId(), result.getAct().getId());
    }

    /**
     * Test the API can return a list of iils for given actor ID
     */
    @Test
    void testGetIilsByActorId() throws Exception {
        doReturn(this.iilList).when(this.iilService).findIilsByActorId(this.testActor1.getId());
        // Perform the MVC request
        MvcResult mvcResult = this.mockMvc.perform(get("/api/iils/actor/{actorId}", this.testActor1.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        IilDto[] result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), IilDto[].class);
        assertEquals(10, Arrays.asList(result).size());
    }


    /*
    @Test
    public void testGetIil() throws Exception {
        UUID id = UUID.randomUUID();
        Iil iil = new Iil();
        iil.setId(id);
        iil.setName("Test Iil");
        iil.setDescription("Test description");
        iil.setHelp(new HashMap<>());
        iil.setAbout(new HashMap<>());

        Mockito.when(iilService.findOne(id)).thenReturn(iil);

        mockMvc.perform(get("/api/iils/{id}", id))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(id.toString())))
                .andExpect(jsonPath("$.name", is("Test Iil")))
                .andExpect(jsonPath("$.description", is("Test description")))
                .andExpect(jsonPath("$.help", isA(Map.class)))
                .andExpect(jsonPath("$.about", isA(Map.class)));

        Mockito.verify(iilService, times(1)).findOne(id);
        Mockito.verifyNoMoreInteractions(iilService);
    }

    @Test
    public void testDeleteIil() throws Exception {
        doNothing().when(iilService).delete(uuid);

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/iils/{id}", uuid)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andDo(MockMvcResultHandlers.print());

        verify(iilService, times(1)).delete(uuid);

 */
}
