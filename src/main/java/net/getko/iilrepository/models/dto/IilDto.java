package net.getko.iilrepository.models.dto;

import lombok.Data;
import net.getko.iilrepository.models.domain.IilStatus;

import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import java.util.UUID;

@Data
public class IilDto implements Serializable {

    private static final long serialVersionUID = -1173956383783083179L;

    private UUID id;

    private String name;

    private String goal;

    private String given;

    private String startWhen;

    private String act;

    private String actor;

    private String endWhen;

    private String produce;

    private String createdBy;

    private String ownedBy;

    private IilStatus status;

    private Date createdAt;

    private Date lastUpdatedAt;

    private List<FlowDto> leadTo;
}
