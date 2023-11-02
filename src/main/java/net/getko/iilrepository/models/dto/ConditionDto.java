package net.getko.iilrepository.models.dto;

import lombok.Data;
import net.getko.iilrepository.models.domain.ConditionType;

import java.util.UUID;

@Data
public class ConditionDto {

    private static final long serialVersionUID = -1173956383783083179L;

    private UUID id;

    private String namespace;

    private String version;

    private String shortName;

    private String name;

    private ConditionType type;

    private String code;
}
