package net.getko.iilrepository.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.getko.iilrepository.TestingConfiguration;
import net.getko.iilrepository.components.DomainDtoMapper;
import net.getko.iilrepository.models.domain.Actor;
import net.getko.iilrepository.models.domain.User;
import net.getko.iilrepository.models.domain.UserGroup;
import net.getko.iilrepository.models.dto.ActorDto;
import net.getko.iilrepository.services.ActorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ActiveProfiles("test")
@WebMvcTest(controllers = UserGroupController.class)
@AutoConfigureMockMvc(addFilters = false)
@ExtendWith(MockitoExtension.class)
@Import(TestingConfiguration.class)
class UserGroupControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ActorService actorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Qualifier("actorDomainToDtoMapper")
    @Autowired
    public DomainDtoMapper actorDomainToDtoMapper;

    @InjectMocks
    private UserGroupController userGroupController;

    private List<UUID> actorIdListWithOnlyNew;

    private List<UUID> actorIdListWithNewAndOld;

    private Actor newActor;

    private UserGroup existingUserGroup;

    private Actor existingActor;

    @BeforeEach
    void setUp() {
        this.newActor = new User();
        this.newActor.setId(UUID.randomUUID());
        this.newActor.setName("new actor");
        this.newActor.setEmail("test_new_actor@test.com");

        this.existingActor = new User();
        this.existingActor.setId(UUID.randomUUID());
        this.existingActor.setName("existing actor");
        this.newActor.setEmail("test_existing_actor@test.com");

        this.existingUserGroup = new UserGroup();
        this.existingUserGroup.setId(UUID.randomUUID());
        this.existingUserGroup.setName("existing user group");
        this.existingUserGroup.setEmail("test_user_group_@test.com");
        Set<Actor> actorList = new HashSet<>();
        actorList.add(this.existingActor);
        this.existingUserGroup.setActorList(actorList);

        this.actorIdListWithOnlyNew = new ArrayList<>();
        this.actorIdListWithOnlyNew.add(this.newActor.getId());

        this.actorIdListWithNewAndOld = new ArrayList<>();
        this.actorIdListWithNewAndOld.add(this.newActor.getId());
        this.actorIdListWithNewAndOld.add(this.existingActor.getId());
    }

    /**
     * Test adding actorListWithOnlyNew that includes a user not exist in the group
     */
    @Test
    void testAddUsersToGroup() throws Exception{
        doReturn(this.existingUserGroup).when(this.actorService).addUsersToGroup(this.existingUserGroup.getId(), this.actorIdListWithOnlyNew);

        // convert the array actorIdListWithOnlyNew to a form like this: ["id1", "id2", "id3"]
        String idList = "[";
        for (int i=0 ; i<this.actorIdListWithOnlyNew.size() ; i++) {
            idList += "\"" + this.actorIdListWithOnlyNew.get(i) + "\"";
            if (i < this.actorIdListWithOnlyNew.size() - 1) {
                idList += ",";
            }
        }
        idList += "]";
        MvcResult mvcResult = this.mockMvc.perform(put("/api/actors/group/{id}", this.existingUserGroup.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(idList))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andReturn();

        ActorDto result = this.objectMapper.readValue(mvcResult.getResponse().getContentAsString(), ActorDto.class);
        assertEquals(this.existingUserGroup.getId(), result.getId());
        assertEquals(true, result.isGroup());
    }

    @Disabled
    void testRemoveUsersFromGroup() {

    }

    @Disabled
    void testRemoveNonExistingUsersFromGroup(){

    }
}
