package net.getko.iilrepository.services;

import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.repositories.IilRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
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
import java.util.Optional;
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

    @InjectMocks
    @Spy
    private IilService iilService;

    @Mock
    private IilRepository iilRepository;

    private List<Iil> iils;

    private Iil newIil;
    private Iil existingIil;

    private UUID testUserID = UUID.randomUUID();

    @BeforeEach
    void setup() {
        this.iils = new ArrayList<>();
        for(long i=0; i<10 ; i++) {
            Iil iil = new Iil();
            iil.setId(UUID.randomUUID());
            iil.setActor(testUserID.toString());
            iil.setAct("Go");
            this.iils.add(iil);
        }

        this.newIil = new Iil();
        this.newIil.setId(UUID.fromString("e91ab6d1-2585-422e-94a7-e538cbf284c3"));
        this.newIil.setActor(testUserID.toString());
        this.newIil.setAct("Go");

        this.existingIil = new Iil();
        this.existingIil.setId(UUID.fromString("2110e38a-c9d4-43e1-ba5c-4741843281d1"));
        this.existingIil.setActor(testUserID.toString());
        this.existingIil.setAct("Stop");
    }

    /**
     * Test that we can retrieve all iils from the database
     */

    @Test
    public void testFindAll() {
        List<Iil> iils = this.iils;
        doReturn(iils).when(this.iilRepository).findAll();

        List<Iil> result = this.iilService.findAll();

        assertEquals(iils.size(), result.size());
    }

    @Test
    public void testFindOne() {
        doReturn(Optional.of(this.existingIil)).when(this.iilRepository).findById(this.existingIil.getId());

        // Perform the service call
        Iil result = this.iilService.findOne(this.existingIil.getId());

        // Make sure the eager relationships repo call was called
        verify(this.iilRepository, times(1)).findById(this.existingIil.getId());

        // Test the result
        assertNotNull(result);
        assertEquals(this.existingIil.getId(), result.getId());
        assertEquals(this.existingIil.getNamespace(), result.getNamespace());
        assertEquals(this.existingIil.getAct(), result.getAct());
        assertEquals(this.existingIil.getActor(), result.getActor());
        assertEquals(this.existingIil.getEndIf(), result.getEndIf());
        assertEquals(this.existingIil.getInput(), result.getInput());
        assertEquals(this.existingIil.getStartIf(), result.getStartIf());
        assertEquals(this.existingIil.getOwner(), result.getOwner());
        assertEquals(this.existingIil.getOutput(), result.getOutput());
        assertEquals(this.existingIil.getStatus(), result.getStatus());
        assertEquals(this.existingIil.getCreatedAt(), result.getCreatedAt());
        assertEquals(this.existingIil.getLastUpdatedAt(), result.getLastUpdatedAt());
    }
}
