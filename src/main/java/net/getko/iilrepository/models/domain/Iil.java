package net.getko.iilrepository.models.domain;

import com.vladmihalcea.hibernate.type.json.JsonBinaryType;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;
import org.hibernate.annotations.TypeDef;
import org.hibernate.annotations.TypeDefs;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.stereotype.Indexed;

import javax.persistence.Basic;
import javax.persistence.Cacheable;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapKeyColumn;
import javax.persistence.MappedSuperclass;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.search.mapper.pojo.mapping.definition.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * The type Iil.
 * <p>
 * A work description.
 * </p>
 * @author Jinki Jung (email: your.jinki.jung@gmail.com)
 */

@TypeDef(name = "jsonb", typeClass = JsonBinaryType.class)
@NoArgsConstructor
@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "iil")
@Indexed
@Cacheable
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Data
public class Iil{
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(generator = "uuid2")
    @GenericGenerator(name = "uuid2", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", columnDefinition = "uuid")
    private UUID id;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "version")
    private String version;

    @Type(type = "jsonb")
    @Column(name = "help", columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Map<String, Object> help;

    @Type(type = "jsonb")
    @Column(name = "about", columnDefinition = "jsonb")
    @Basic(fetch = FetchType.LAZY)
    private Map<String, Object> about;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "state")
    private IilState state;

    @Column(name = "goal")
    private UUID goal;

    @Column(name = "next")
    @OneToMany
    private List<NextFlow> next;
    // 네임스페이스 존재, 호환성 보장

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "namespace")
    private String namespace;

    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "activateif")
    private String activateIf;
    // 결과: 상태 변경, 보고

    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "input")
    private String input;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "act")
    private String act;
    // 네임스페이스 존재, 호환성 보장

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "actor")
    private String actor;

    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "finishIf")
    private String finishIf;
    // 결과: 상태 변경, 보고, 플로우 시작

    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "output")
    private String output;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "creator")
    private String creator;

    @NotNull
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "owner")
    private String owner;

    @LastModifiedDate
    @KeywordField(sortable = org.hibernate.search.engine.backend.types.Sortable.YES)
    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    public String getIdAsString() {
        return this.id.toString();
    }
}
