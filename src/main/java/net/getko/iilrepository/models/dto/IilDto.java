package net.getko.iilrepository.models.dto;

import lombok.Data;
import net.getko.iilrepository.models.domain.IilState;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class IilDto implements Serializable {

    private static final long serialVersionUID = -1173956383783083179L;

    private UUID id;

    private String namespace;

    private String version;

    private Map<String, Object> help; // when user request

    private Map<String, Object> about; //description

    private UUID goal; // for what

    private Map<String, Object> input;

    private ConditionDto activateIf;

    private ActionDto act;

    private String actor;

    private ConditionDto finishIf;

    private Map<String, Object> output;

    private Map<String, Object> variable;

    private String maintainer;

    private String owner;

    private IilState state;

    private ConditionDto abortIf;

    private LocalDateTime updatedAt;

    private Map<String, Object> next;
}
