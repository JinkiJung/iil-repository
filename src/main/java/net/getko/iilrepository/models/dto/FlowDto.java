package net.getko.iilrepository.models.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Map;
import java.util.UUID;

@Data
public class FlowDto implements Serializable {
    private UUID id;

    private Map<String, String> describe;
}
