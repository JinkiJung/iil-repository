package net.getko.iilrepository.models.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
public class NextFlowDto implements Serializable {
    private UUID id;

    private Map<String, String> describe;

    private String namespace;

    private String input;

    private String condition;

    private String creator;

    private String owner;

    private UUID from;

    private UUID to;
}
