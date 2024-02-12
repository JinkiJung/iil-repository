package net.getko.iilrepository.services;

import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.models.domain.Act;
import net.getko.iilrepository.repositories.ActRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ActServiceTest {
    @InjectMocks
    @Spy
    private ActService actService;

    @Mock
    private ActRepository actRepository;

    private List<Act> acts;
    private Act newAct;
    private Act existingAct;

    @BeforeEach
    void setup() {
        this.acts = new ArrayList<>();
        for(long i=0 ; i<15 ; i++) {
            Act act = new Act();
            // id should be UUID
            act.setId(UUID.randomUUID());
            act.setName("Act " + i);
            act.setShortName("act_"+i);
            this.acts.add(act);
        }

        this.newAct = new Act();
        this.newAct.setId(UUID.randomUUID());
        this.newAct.setName("New Act");
        this.newAct.setShortName("new_act");

        this.existingAct = new Act();
        this.existingAct.setId(UUID.randomUUID());
        this.existingAct.setName("Existing Act");
        this.existingAct.setShortName("existing_act");

    }

    /**
     * Test that a single action can be returned
     */
    @Test
    void testFindOne() throws DataNotFoundException {
        // this code is to perform test to find one service
        doReturn(Optional.of(this.existingAct)).when(this.actRepository).findById(this.existingAct.getId());

        // perform act api call and get Act
        Act act = this.actService.findById(this.existingAct.getId());

        // verify that exisingAction's ID is stored as one entry in actRepository
        verify(this.actRepository, times(1)).findById(this.existingAct.getId());


        assertNotNull(existingAct);
        assertEquals(this.existingAct.getId(), act.getId());
        assertEquals(this.existingAct.getName(), act.getName());
        assertEquals(this.existingAct.getShortName(), act.getShortName());

    }

    /**
     * Test that a list of all acts can be returned
     */
    @Test
    void testFindAll() {
        // this code is to perform test to find all service
        doReturn(this.acts).when(this.actRepository).findAll();

        // perform action api call
        List<Act> acts = this.actService.findAll();

        assertNotNull(acts);
        assertEquals(this.acts.size(), acts.size());
    }
}
