package net.getko.iilrepository.models.domain;

import lombok.Data;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Indexed;

import javax.persistence.Cacheable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Set;
import java.util.UUID;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "actor")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@Inheritance(strategy = InheritanceType.JOINED)
public class Actor {
    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "uuid")
    protected UUID id;

    @Column
    @NotNull
    protected String name;

    @Column
    @NotNull
    protected String email;

    @Column
    @NotNull
    protected boolean isGroup;

    @Column
    protected String iconLink;

    @OneToMany(cascade = CascadeType.ALL)
    protected Set<Actor> actorList;

    public Actor() {

    }

    // override hashcode and equals method in Actor class
    // to make sure that two actors with the same id are equal

    @Override
    public int hashCode() {
        return this.id.hashCode();
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Actor)) return false;
        return id != null && id.equals(((Actor) o).getId());
    }
}
