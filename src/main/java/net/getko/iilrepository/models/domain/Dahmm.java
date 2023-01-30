package net.getko.iilrepository.models.domain;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.search.mapper.pojo.mapping.definition.annotation.KeywordField;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Indexed;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Map;
import java.util.UUID;

@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "dahmm")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Dahmm {

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @Type(type = "jsonb")
    @Column(name = "about", columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Map<String, Object> about;

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
    @Column(name = "maintainer")
    private String maintainer;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "owner")
    private String owner;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "iilfrom")
    private UUID iilFrom;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "iilto")
    private UUID iilTo;
}
