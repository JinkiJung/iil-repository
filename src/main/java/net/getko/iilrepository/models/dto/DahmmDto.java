package net.getko.iilrepository.models.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
public class DahmmDto implements Serializable {
    private UUID id;

    private Map<String, Object> about; //description

    private String version;

    private String namespace;

    private String input;

    private String condition;

    private String maintainer;

    private String owner;

    private UUID iilFrom;

    private UUID iilTo;
}
