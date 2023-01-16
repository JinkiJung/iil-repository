package net.getko.iilrepository.models.domain;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Indexed;

import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "next_flow")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class NextFlow {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @ElementCollection
    @CollectionTable(name = "next_flow_describe_mapping",
            joinColumns = {@JoinColumn(name = "describe_id", referencedColumnName = "id")})
    @MapKeyColumn(name = "key")
    @Column(name = "value")
    private Map<String, String> describe;

    @NotNull
    @Column(name = "version")
    private String version;

    @NotNull
    @Column(name = "namespace")
    private String namespace;

    @Column(name = "input")
    private String input;

    @Column(name = "condition")
    private String condition;

    @NotNull
    @Column(name = "creator")
    private String creator;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "owner")
    private String owner;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "from_iil")
    private UUID from;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "to_iil")
    private UUID to;
}
