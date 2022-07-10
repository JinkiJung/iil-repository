package net.getko.iilrepository.models.domain;

import com.sun.istack.NotNull;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Indexed;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

/**
 * The type Iil.
 * <p>
 * An unit for work.
 * </p>
 * @author Jinki Jung (email: your.jinki.jung@gmail.com)
 */
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "iil")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
@Getter
@Setter
public class Iil{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @NotNull
    @FullTextField
    @KeywordField(name = "name_sort", normalizer = "lowercase", sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "name")
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "goal_id")
    private Iil goal;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "start_when")
    private String startWhen;

    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "given")
    private String given;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "act")
    private String act;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "actor")
    private String actor;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "end_when")
    private String endWhen;

    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "produce")
    private String produce;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "created_by")
    private String createdBy;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "owned_by")
    private String ownedBy;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "status")
    private IilStatus status;

    @CreatedDate
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "last_updated_at")
    private LocalDateTime lastUpdatedAt;

    @Column(name = "lead_to")
    @OneToMany
    private List<Flow> leadTo;

    public String getIdAsString() {
        return this.id.toString();
    }
}
