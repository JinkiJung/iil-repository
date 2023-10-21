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
    @OneToMany(cascade = CascadeType.ALL)
    private Set<User> userList;

    public UserGroup() {
        this.isGroup = true;
        this.userList = new HashSet<>();
    }

    public void addUser(User user) {
        this.userList.add(user);
    }

    public void removeUser(User user) throws DataNotFoundException {
        // throws exception when user does not in the userList
        if (this.userList.contains(user)) {
            this.userList.remove(user);
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
