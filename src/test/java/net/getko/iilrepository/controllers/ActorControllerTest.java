package net.getko.iilrepository.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.getko.iilrepository.TestingConfiguration;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Act;
import net.getko.iilrepository.models.domain.Actor;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.models.domain.User;
import net.getko.iilrepository.models.domain.UserGroup;
import net.getko.iilrepository.models.dto.ActorDto;
import net.getko.iilrepository.services.ActorService;
import net.getko.iilrepository.services.IilService;
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
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = ActorController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(TestingConfiguration.class)
public class ActorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActorService actorService;

    @MockBean
    private IilService iilService;

    @Autowired
    private ObjectMapper objectMapper;

    @Qualifier("actorDomainToDtoMapper")
    @Autowired
    public DomainDtoMapper actorDomainToDtoMapper;

    @InjectMocks
    private ActorController actorController;

    private List<Actor> actorList;

    private Actor newActor;

    private UserGroup newUserGroup;

    private Actor existingActor;

    private Iil existingIil;

    @BeforeEach
    void setUp() {
        this.actorList = new ArrayList<>();
        for(int i=0 ; i< 10 ; i++) {
            Actor actor = new User();
            actor.setId(UUID.randomUUID());
            actor.setName("test actor " + i);
            actor.setEmail("test" + i + "@test.com");
            this.actorList.add(actor);
        }

        this.newActor = new User();
        this.newActor.setId(UUID.randomUUID());
        this.newActor.setName("new actor");
        this.newActor.setEmail("test_new_actor@test.com");

        this.existingActor = new User();
        this.existingActor.setId(UUID.randomUUID());
        this.existingActor.setName("existing actor");
        this.newActor.setEmail("test_existing_actor@test.com");

        this.newUserGroup = new UserGroup();
        this.newUserGroup.setId(UUID.randomUUID());
        this.newUserGroup.setName("new user group");
        this.newUserGroup.setEmail("test_user_group_@test.com");
        Set<Actor> actorList = new HashSet<>();
        actorList.add(this.existingActor);
        this.newUserGroup.setActorList(actorList);

        this.existingIil = new Iil();
        this.existingIil.setId(UUID.randomUUID());
        this.existingIil.setAct(new Act("Test existing"));
        this.existingIil.setActor(this.existingActor);
    }

    /**
     * Test the API can return a list of all actors.
     */
    @Test
    void testGetFullActorList() throws Exception {
        doReturn(this.actorList).when(this.actorService).findAll();

        MvcResult mvcResult = this.mockMvc.perform(get("/api/actors"))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        ActorDto[] result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ActorDto[].class);
        assertEquals(this.actorList.size(), result.length);
    }

    /**
     * Test the API can return a single actor.
     */
    @Test
    void testGetAnActorForGivenId() throws Exception {
        doReturn(this.existingActor).when(this.actorService).findById(this.existingActor.getId());

        MvcResult mvcResult = this.mockMvc.perform(get("/api/actors/" + this.existingActor.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        ActorDto result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ActorDto.class);
        assertEquals(this.existingActor.getId(), result.getId());
    }

    /**
     * Test that a user can be created through the API
     */
    @Test
    void testCreateAnUser() throws Exception {
        doReturn(this.newActor).when(this.actorService).save(any());

        String contentStr = this.objectMapper.writeValueAsString(this.actorDomainToDtoMapper.convertTo(this.newActor, ActorDto.class));
        MvcResult mvcResult = this.mockMvc.perform(post("/api/actors/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(contentStr))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        ActorDto result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ActorDto.class);
        assertEquals(this.newActor.getId(), result.getId());
    }

    /**
     * Test that a user group can be created through the API
     */
    @Test
    void testCreateAnUserGroup() throws Exception{
        doReturn((Actor)this.newUserGroup).when(this.actorService).save(any());

        String contentStr = this.objectMapper.writeValueAsString(this.actorDomainToDtoMapper.convertTo(this.newUserGroup, ActorDto.class));
        MvcResult mvcResult = this.mockMvc.perform(post("/api/actors/")
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .content(contentStr))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andReturn();

        ActorDto result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ActorDto.class);
        assertEquals(this.newUserGroup.getId(), result.getId());
        assertEquals(true, result.isGroup());
        assertEquals(1, result.getActorList().size());
    }

    /**
     * Test that an actor can be deleted through the API
     */
    @Test
    void testDeleteAnActor() throws Exception{
        doReturn(this.existingActor).when(this.actorService).findById(this.existingActor.getId());

        MvcResult mvcResult = this.mockMvc.perform(delete("/api/actors/" + this.existingActor.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isOk())
                .andReturn();
    }

    /**
     * Test that an actor who doesn't exist throws error
     */
    @Test
    void testDeleteNonExistActor() throws Exception{
        doReturn(null).when(this.actorService).findById(this.existingActor.getId());

        this.mockMvc.perform(delete("/api/actors/" + this.existingActor.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isNotFound())
                .andReturn();
    }

    /**
     * Test that an actor can not be deleted if it has any iils
     */
    @Test
    void testDeleteAnActorWithIils() throws Exception {
        // return list filled with this.existingIil
        doReturn(this.existingActor).when(this.actorService).findById(this.existingActor.getId());
        doReturn(List.of(this.existingIil)).when(this.iilService).findIilsByActorId(this.existingActor.getId());

        this.mockMvc.perform(delete("/api/actors/" + this.existingActor.getId()))
                .andDo(MockMvcResultHandlers.print())
                .andExpect(status().isUnprocessableEntity())
                .andReturn();
    }
}
