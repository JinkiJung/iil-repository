package net.getko.iilrepository.services;

import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.repositories.IilRepository;
import org.junit.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class IilServiceTest {

    @Autowired
    private MockMvc mockMvc;

    @InjectMocks
    @Spy
    private IilService iilService;

    @Mock
    private IilRepository iilRepository;

    private List<Iil> iils;

    private Iil newIil;
    private Iil existingIil;

    @BeforeEach
    void setup() {
        this.iils = new ArrayList<>();
        for(long i=0; i<10 ; i++) {
            Iil iil = new Iil();
            iil.setId(UUID.randomUUID());
            iil.setName("Test");
            iil.setAct("Go");
            this.iils.add(iil);
        }

        this.newIil = new Iil();
        this.newIil.setId(UUID.fromString("e91ab6d1-2585-422e-94a7-e538cbf284c3"));
        this.newIil.setName("New iil");
        this.newIil.setAct("Go");

        this.existingIil = new Iil();
        this.existingIil.setId(UUID.fromString("2110e38a-c9d4-43e1-ba5c-4741843281d1"));
        this.existingIil.setName("Existing iil");
        this.existingIil.setAct("Stop");
    }

    /*
    @Test
    void testFindAll() {

        MvcResult mvcResult = this.mockMvc.perform(get("/api/iils"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(header().string("X-Total-Count", Long.toString(iils.length())))
                .andExpect(header().exists(HttpHeaders.LINK))
                .andReturn();
    }
     */

    @Test
    void testFindOne() {
        doReturn(this.existingIil).when(iilRepository).findById(this.existingIil.getId());

        // Perform the service call
        Iil result = this.iilService.findOne(this.existingIil.getId());

        // Make sure the eager relationships repo call was called
        verify(this.iilRepository, times(1)).findById(this.existingIil.getId());

        // Test the result
        assertNotNull(result);
        assertEquals(this.existingIil.getId(), result.getId());
        assertEquals(this.existingIil.getName(), result.getName());
        assertEquals(this.existingIil.getAct(), result.getAct());
        assertEquals(this.existingIil.getActor(), result.getActor());
        assertEquals(this.existingIil.getEndWhen(), result.getEndWhen());
        assertEquals(this.existingIil.getGiven(), result.getGiven());
        assertEquals(this.existingIil.getStartWhen(), result.getStartWhen());
        assertEquals(this.existingIil.getOwnedBy(), result.getOwnedBy());
        assertEquals(this.existingIil.getProduce(), result.getProduce());
        assertEquals(this.existingIil.getStatus(), result.getStatus());
        assertEquals(this.existingIil.getCreatedBy(), result.getCreatedBy());
        assertEquals(this.existingIil.getCreatedAt(), result.getCreatedAt());
        assertEquals(this.existingIil.getLastUpdatedAt(), result.getLastUpdatedAt());
    }
}
