package net.getko.iilrepository.models.dto;

import lombok.Data;
import net.getko.iilrepository.models.domain.IilStatus;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@Data
public class IilDto implements Serializable {

    private static final long serialVersionUID = -1173956383783083179L;

    private UUID id;

    private Map<String, String> describe;

    private IilDto goal;

    private String input;

    private String namespace;

    private String startIf;

    private String act;

    private String actor;

    private String endIf;

    private String output;

    private String owner;

    private IilStatus status;

    private LocalDateTime createdAt;

    private LocalDateTime lastUpdatedAt;

    private List<NextFlowDto> next;
}
