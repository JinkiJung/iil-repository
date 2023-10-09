package net.getko.iilrepository.services;

import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.models.domain.Action;
import net.getko.iilrepository.repositories.ActionRepository;
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
public class ActionServiceTest {
    @InjectMocks
    @Spy
    private ActionService actionService;

    @Mock
    private ActionRepository actionRepository;

    private List<Action> actions;
    private Action newAction;
    private Action existingAction;

    @BeforeEach
    void setup() {
        this.actions = new ArrayList<>();
        for(long i=0 ; i<15 ; i++) {
            Action action = new Action();
            // id should be UUID
            action.setId(UUID.randomUUID());
            action.setName("Action " + i);
            action.setShortName("act_"+i);
            this.actions.add(action);
        }

        this.newAction = new Action();
        this.newAction.setId(UUID.randomUUID());
        this.newAction.setName("New Action");
        this.newAction.setShortName("new_act");

        this.existingAction = new Action();
        this.existingAction.setId(UUID.randomUUID());
        this.existingAction.setName("Existing Action");
        this.existingAction.setShortName("existing_act");

    }

    /**
     * Test that a single action can be returned
     */
    @Test
    void testFindOne() throws DataNotFoundException {
        // this code is to perform test to find one service
        doReturn(Optional.of(this.existingAction)).when(this.actionRepository).findById(this.existingAction.getId());

        // perform action api call and get Action
        Action action = this.actionService.findById(this.existingAction.getId());

        // verify that exisingAction's ID is stored as one entry in actionRepository
        verify(this.actionRepository, times(1)).findById(this.existingAction.getId());


        assertNotNull(existingAction);
        assertEquals(this.existingAction.getId(), action.getId());
        assertEquals(this.existingAction.getName(), action.getName());
        assertEquals(this.existingAction.getShortName(), action.getShortName());

    }

    /**
     * Test that a list of all actions can be returned
     */
    @Test
    void testFindAll() {
        // this code is to perform test to find all service
        doReturn(this.actions).when(this.actionRepository).findAll();

        // perform action api call
        List<Action> actions = this.actionService.findAll();

        assertNotNull(actions);
        assertEquals(this.actions.size(), actions.size());
    }
}
