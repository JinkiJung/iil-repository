package net.getko.iilrepository.models.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

@Data
public class ActorDto implements Serializable {
    private UUID id;
    private String name;
    private String email;
    private String iconLink;
    private boolean isGroup;
    private Set<IilDto> iilList;
    private Set<ActorDto> userList;
}
