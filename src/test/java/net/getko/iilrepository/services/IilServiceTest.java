package net.getko.iilrepository.services;

import net.getko.iilrepository.exceptions.ActionValidationException;
import net.getko.iilrepository.exceptions.ConditionValidationException;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.models.domain.Iil;
import net.getko.iilrepository.repositories.IilRepository;
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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;

@ExtendWith(MockitoExtension.class)
public class IilServiceTest {

    @InjectMocks
    @Spy
    private IilService iilService;

    @Mock
    private IilRepository iilRepository;

    private List<Iil> iilList;

    private Iil newIil;
    private Iil existingIil;

    private UUID testUser1 = UUID.randomUUID();
    private UUID testUser2 = UUID.randomUUID();

    @BeforeEach
    void setup() {
        this.iilList = new ArrayList<>();
        for(long i=0; i<10 ; i++) {
            Iil iil = new Iil();
            iil.setId(UUID.randomUUID());
            iil.setActor(testUser1.toString());
            this.iilList.add(iil);
        }

        this.newIil = new Iil();
        this.newIil.setId(UUID.fromString("e91ab6d1-2585-422e-94a7-e538cbf284c3"));
        this.newIil.setActor(testUser1.toString());

        this.existingIil = new Iil();
        this.existingIil.setId(UUID.fromString("2110e38a-c9d4-43e1-ba5c-4741843281d1"));
        this.existingIil.setActor(testUser1.toString());
    }

    /**
     * Test that we can retrieve all iils from the database
     */

    @Test
    public void testFindAll() {
        List<Iil> iils = this.iilList;
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
        assertEquals(this.existingIil.getFinishIf(), result.getFinishIf());
        assertEquals(this.existingIil.getInput(), result.getInput());
        assertEquals(this.existingIil.getActivateIf(), result.getActivateIf());
        assertEquals(this.existingIil.getOwner(), result.getOwner());
        assertEquals(this.existingIil.getOutput(), result.getOutput());
        assertEquals(this.existingIil.getState(), result.getState());
        assertEquals(this.existingIil.getUpdatedAt(), result.getUpdatedAt());
        assertEquals(this.existingIil.getNext(), result.getNext());
    }

    /**
     * Test that when there is a validation error in the provided iil
     * object, the saving operation will fail and not continue.
     */
    @Test
    void testSaveValidationError() throws ConditionValidationException, ActionValidationException, DataNotFoundException {
        doThrow(ConditionValidationException.class).when(this.iilService).validateIil(any());

        // Perform the service call
        assertThrows(ConditionValidationException.class, () ->
                this.iilService.save(this.newIil)
        );

        // And also that no saving calls took place in the repository
        verify(this.iilRepository, never()).save(any());
    }

    /**
     * Test that when we are trying to update an iil but the
     * provided ID is invalid, a DataNotFoundException will be thrown.
     */
    @Test
    void testSaveNoValidId() {
        doReturn(Optional.empty()).when(this.iilRepository).findById(this.existingIil.getId());

        // Perform the service call
        assertThrows(DataNotFoundException.class, () ->
                this.iilService.save(this.existingIil)
        );

        // And also that no saving calls took place in the repository
        verify(this.iilRepository, never()).save(any());
    }
}
