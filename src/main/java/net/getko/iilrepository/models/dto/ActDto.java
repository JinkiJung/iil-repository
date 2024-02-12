package net.getko.iilrepository.models.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class ActDto {

    private UUID id;

    private String namespace;

    private String version;

    private String shortName;

    private String name;

    private String code;
}
