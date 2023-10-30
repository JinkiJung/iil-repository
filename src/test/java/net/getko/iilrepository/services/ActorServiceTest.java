package net.getko.iilrepository.services;

import net.getko.iilrepository.exceptions.DataNotFoundException;
import net.getko.iilrepository.models.domain.Actor;
import net.getko.iilrepository.models.domain.User;
import net.getko.iilrepository.models.domain.UserGroup;
import net.getko.iilrepository.repositories.UserGroupRepository;
import net.getko.iilrepository.repositories.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DuplicateKeyException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.hibernate.search.util.common.impl.Contracts.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class ActorServiceTest {
    @InjectMocks
    @Spy
    private ActorService actorService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserGroupRepository userGroupRepository;

    private List<Actor> users;
    private Actor newUser;
    private Actor newUserGroup;
    private Actor existingUser;
    private Actor existingUserGroup;

    // string array of random names
    private String[] names = {"John", "Jane", "Jack", "Jill", "James", "Jenny", "Jasper", "Jade", "Jared", "Jasmine"};

    @BeforeEach
    void setup() {
        this.users = new ArrayList<>();
        for(int i=0 ; i< names.length ; i++) {
            Actor actor;
            if (i < names.length/2) {
                actor = new User();
                // id should be UUID
            } else {
                actor = new UserGroup();
                // id should be UUID
            }
            actor.setId(UUID.randomUUID());
            actor.setName(names[i]);
            actor.setEmail(names[i].toLowerCase()+"@example.com");
            this.users.add(actor);
        }

        this.newUser = new User();
        this.newUser.setId(UUID.randomUUID());
        this.newUser.setName("New User");
        this.newUser.setEmail("new@example.com");

        this.existingUser = new User();
        this.existingUser.setId(UUID.randomUUID());
        this.existingUser.setName("Existing User");
        this.existingUser.setEmail("existing@example.com");

        this.newUserGroup = new UserGroup();
        this.newUserGroup.setId(UUID.randomUUID());
        this.newUserGroup.setName("New User Group");
        this.newUserGroup.setEmail("new_group@example.com");

        this.existingUserGroup = new UserGroup();
        this.existingUserGroup.setId(UUID.randomUUID());
        this.existingUserGroup.setName("Existing User Group");
        this.existingUserGroup.setEmail("existing_group@example.com");
        ((UserGroup)this.existingUserGroup).getActorList().add((User)this.existingUser);
    }

    /**
     * Test that a single user can be returned
     */
    @Test
    void testFindSingleUser() throws DataNotFoundException {
        // Mock the repository call
        doReturn(Optional.of(this.existingUser)).when(this.userRepository).findById(this.existingUser.getId());

        // Execute the service call
        Actor returnedUser = this.actorService.findById(this.existingUser.getId());

        // Assert the response
        assertNotNull(returnedUser, "User was not found");
        assertEquals(this.existingUser, returnedUser, "User returned was not the same as the mock");
    }

    /**
     * Test that an error thrown when a user is not found
     */
    @Test
    void testFindSingleUserNotFound() {
        // Mock the repository call
        doReturn(Optional.empty()).when(this.userRepository).findById(this.existingUser.getId());

        // Execute the service call
        try {
            this.actorService.findById(this.existingUser.getId());
        } catch (DataNotFoundException e) {
            assertEquals("No actor found for the provided ID", e.getMessage());
        }
    }

    /**
     * Test that a single user group can be returned
     */
    @Test
    void testFindSingleUserGroup() throws DataNotFoundException {
        // Mock the repository call
        doReturn(Optional.of(this.existingUserGroup)).when(this.userGroupRepository).findById(this.existingUserGroup.getId());

        // Execute the service call
        Actor returnedUserGroup = this.actorService.findById(this.existingUserGroup.getId());

        // Assert the response
        assertNotNull(returnedUserGroup, "User group was not found");
        assertEquals(this.existingUserGroup, returnedUserGroup, "User group returned was not the same as the mock");
    }

    /**
     * Test that a single user can be added to a user group
     */
    @Test
    void testAddUserToGroup() throws DataNotFoundException {
        doAnswer(i -> i.getArguments()[0]).when(this.userGroupRepository).save(any());
        doReturn(Optional.of(this.newUser)).when(this.userRepository).findById(this.newUser.getId());
        doReturn(Optional.of(this.existingUserGroup)).when(this.userGroupRepository).findById(this.existingUserGroup.getId());

        // Execute the service call
        Actor returnedUserGroup = this.actorService.addUserToGroup(this.existingUserGroup.getId(), this.newUser.getId());

        // Assert the response
        assertNotNull(returnedUserGroup, "User group was not found");
        assertEquals(this.existingUserGroup, returnedUserGroup, "User group returned was not the same as the mock");
        assertEquals(2, ((UserGroup)returnedUserGroup).getActorList().size(), "User group did not have the expected number of members");
        // check if the existing user is contained in user group
        assertEquals(true, (((UserGroup)returnedUserGroup).getActorList().contains(this.existingUser)), "User group did not have the expected member");
    }

    /**
     * Test that duplicated addition of users throws an error
     */
    @Test
    void testAddDuplicatedUser() throws DuplicateKeyException {
        doReturn(Optional.of(this.existingUser)).when(this.userRepository).findById(this.existingUser.getId());
        doReturn(Optional.of(this.existingUserGroup)).when(this.userGroupRepository).findById(this.existingUserGroup.getId());

        // Execute the service call
        assertThrows(DuplicateKeyException.class, () ->
                this.actorService.addUserToGroup(this.existingUserGroup.getId(), this.existingUser.getId())
        );
    }

    /**
     * Test that a user can be added to user
     */
    @Test
    void testAddUserToUser() throws DataNotFoundException {
        assertThrows(DataNotFoundException.class, () ->
                this.actorService.addUserToGroup(this.newUser.getId(), this.existingUser.getId())
        );
    }

    /**
     * Test that a duplicate error thrown when existing user is added to a user group
     */
    @Test
    void testAddExistingUserToGroup() {
        doReturn(Optional.of(this.existingUser)).when(this.userRepository).findById(this.existingUser.getId());
        doReturn(Optional.of(this.existingUserGroup)).when(this.userGroupRepository).findById(this.existingUserGroup.getId());

        // Execute the service call
        assertThrows(DuplicateKeyException.class, () ->
                this.actorService.addUserToGroup(this.existingUser.getId(), this.existingUserGroup.getId())
        );
    }

    /**
     * Test that a user can be removed from a user group
     */
    @Test
    void testRemoveUserFromUserGroup() {
        doReturn(Optional.of(this.existingUser)).when(this.userRepository).findById(this.existingUser.getId());
        doReturn(Optional.of(this.existingUserGroup)).when(this.userGroupRepository).findById(this.existingUserGroup.getId());

        // Execute the service call
        this.actorService.removeUserFromGroup(this.existingUserGroup.getId(), this.existingUser.getId());

        // Assert the response
        assertEquals(0, ((UserGroup)this.existingUserGroup).getActorList().size(), "User group did not have the expected number of members");
    }

    /**
     * Test that an error thrown when a user is not found in a user group
     */
    @Test
    void testRemoveNonExistingUserFromUserGroup() {
        doReturn(Optional.of(this.newUser)).when(this.userRepository).findById(this.newUser.getId());
        doReturn(Optional.of(this.existingUserGroup)).when(this.userGroupRepository).findById(this.existingUserGroup.getId());

        // Execute the service call
        assertThrows(DataNotFoundException.class, () ->
                this.actorService.removeUserFromGroup(this.existingUserGroup.getId(), this.newUser.getId())
        );
    }

    /**
     * Test that the api call can fetch all actors, including 5 users and 5 user groups
     */
    @Test
    void testFindAll() {
        // Mock the repository call
        doReturn(this.users).when(this.actorService).findAll();

        // Execute the service call
        List<Actor> returnedUsers = this.actorService.findAll();

        // Assert the response
        assertNotNull(returnedUsers, "Users were not found");
        assertEquals(this.users.size(), returnedUsers.size(), "Users returned were not the same as the mock");
        // find there are five users in returnedUsers
        assertEquals(5, returnedUsers.stream().filter(user -> user instanceof User).count(), "Users returned were not the same as the mock");
        // find there are five user groups in returnedUsers
        assertEquals(5, returnedUsers.stream().filter(user -> user instanceof UserGroup).count(), "Users returned were not the same as the mock");
    }

    /**
     * Test including deletion of user does not exist
     */
    @Test
    void testDeleteNotExistUser() {
        // Mock the repository call
        doReturn(Optional.empty()).when(this.userRepository).findById(this.existingUser.getId());

        // Execute the service call
        assertThrows(DataNotFoundException.class, () ->
                this.actorService.delete(this.existingUser.getId())
        );
    }

    /**
     * Test including deletion of user in users
     */
    @Test
    void testDeleteForUser() {
        // Mock the repository call
        doReturn(Optional.of(this.existingUser)).when(this.userRepository).findById(this.existingUser.getId());
        doNothing().when(this.userRepository).deleteById(this.existingUser.getId());

        // Execute the service call
        this.actorService.delete(this.existingUser.getId());

        // Verify that a deletion call took place in the repository
        verify(this.userRepository, times(1)).deleteById(this.existingUser.getId());
    }

    /**
     * Test user creation
     */
    @Test
    void testUserCreation() {
        // Mock the repository call
        doAnswer(i -> i.getArguments()[0]).when(this.userRepository).save(any());

        // Execute the service call
        Actor returnedUser = this.actorService.save(this.newUser);

        // Assert the response
        assertNotNull(returnedUser, "User was not created");
        assertEquals(this.newUser, returnedUser, "User returned was not the same as the mock");
    }

    @Disabled
    void testUserUpdate() {

    }

    @Disabled
    void testUserGroupCreation() {

    }

    @Disabled
    void testAddNewUsersToUserGroup(){

    }

    @Disabled
    void testExistingUsersToUserGroup(){

    }

    @Disabled
    void testRemoveUsersFromUserGroup(){

    }

    @Disabled
    void testRemoveNonExistingUsersFromUserGroup(){

    }

    @Disabled
    void testUserGroupDeletion() {

    }

    @Disabled
    void testUserGroupDeletionCascade() {

    }
}
