package net.getko.iilrepository.models.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ConditionDto {

    private static final long serialVersionUID = -1173956383783083179L;

    private UUID id;

    private String namespace;

    private String shortName;

    private String name;

    private String type;

    private String code;
}
