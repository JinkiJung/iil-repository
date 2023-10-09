package net.getko.iilrepository.services;

import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.models.domain.Condition;
import net.getko.iilrepository.models.domain.ConditionType;
import net.getko.iilrepository.repositories.ConditionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ConditionServiceTest {
    @InjectMocks
    @Spy
    private ConditionService conditionService;

    @Mock
    private ConditionRepository conditionRepository;

    private List<Condition> conditions;

    private Condition newCondition;
    private Condition existingCondition;

    // string array filled with ConditionType values
    private List<ConditionType> conditionTypes = Arrays.asList(ConditionType.values());

    @BeforeEach
    void setup() {
        this.conditions = new ArrayList<>();
        for(long i=0 ; i<15 ; i++) {
            Condition condition = new Condition();
            // id should be UUID
            condition.setId(UUID.randomUUID());
            condition.setName("Condition " + i);
            condition.setType(conditionTypes.get((int) (i % conditionTypes.size())));
            condition.setShortName("cond_"+i);
            this.conditions.add(condition);
        }

        this.newCondition = new Condition();
        this.newCondition.setId(UUID.randomUUID());
        this.newCondition.setName("New Condition");
        this.newCondition.setType(conditionTypes.get(0));
        this.newCondition.setShortName("new_cond");

        this.existingCondition = new Condition();
        this.existingCondition.setId(UUID.randomUUID());
        this.existingCondition.setName("Existing Condition");
        this.existingCondition.setType(conditionTypes.get(1));
        this.existingCondition.setShortName("existing_cond");

    }

    /**
     * Test that a single condition can be returned
     */
    @Test
    void testFindOne() throws DataNotFoundException {
        doReturn(Optional.of(this.existingCondition)).when(this.conditionRepository).findById(this.existingCondition.getId());

        Condition result = this.conditionService.findById(this.existingCondition.getId());

        verify(this.conditionRepository, times(1)).findById(this.existingCondition.getId());

        assertNotNull(existingCondition);
        assertEquals(this.existingCondition.getId(), result.getId());
        assertEquals(this.existingCondition.getName(), result.getName());
        assertEquals(this.existingCondition.getShortName(), result.getShortName());
        assertEquals(this.existingCondition.getType(), result.getType());
    }

    /**
     * Test that a list of all conditions can be returned
     */
    @Test
    void testFindAll() {
        doReturn(this.conditions).when(this.conditionRepository).findAll();

        List<Condition> result = this.conditionService.findAll();

        assertNotNull(conditions);
        assertEquals(this.conditions.size(), result.size());
    }

    /**
     * Test that a list of conditions can be returned by type
     */
    @Test
    void testFindByType() throws DataNotFoundException {
        doReturn(this.conditions).when(this.conditionRepository).findByType(this.existingCondition.getType());

        List<Condition> result = this.conditionService.findByType(this.existingCondition.getType());

        assertNotNull(conditions);
        assertEquals(this.conditions.size(), result.size());
    }
}
