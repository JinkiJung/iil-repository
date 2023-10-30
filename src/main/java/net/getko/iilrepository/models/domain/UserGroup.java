package net.getko.iilrepository.models.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import net.getko.iilrepository.exceptions.DataNotFoundException;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Indexed;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import java.util.HashSet;
import java.util.Set;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "iiluser_group")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class UserGroup extends Actor {

    public UserGroup() {
        this.isGroup = true;
        this.actorList = new HashSet<>();
    }

    public UserGroup(Actor actor) {
        this.id = actor.getId();
        this.name = actor.getName();
        this.email = actor.getEmail();
        this.isGroup = actor.isGroup();
        this.iconLink = actor.getIconLink();
        if (actor.getActorList() != null) {
            this.actorList = actor.getActorList();
        } else {
            this.actorList = new HashSet<>();
        }
    }

    public void addUser(User user) {
        // throw exception if user's isGroup is true
        if (user.isGroup()) {
            throw new IllegalArgumentException("UserGroup can't be added to another UserGroup");
        }

        // if actorList does not contain user with same id, add user to the actorList
        this.actorList.stream().filter(actor -> actor.getId().equals(user.getId())).findFirst().ifPresentOrElse(
                actor -> {
                    // do nothing
                },
                () -> {
                    this.actorList.add(user);
                }
        );
    }

    public void removeUser(User user) throws DataNotFoundException {
        // throws exception when user does not in the userList
        if (this.actorList.contains(user)) {
            this.actorList.remove(user);
        } else {
            throw new DataNotFoundException("User does not exist in the group");
        }
    }

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Actor)) return false;
        return this.id != null && this.id.equals(((Actor) o).getId());
    }
}
