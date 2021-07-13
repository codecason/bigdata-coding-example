package imooc.naga.server.controller;

import imooc.naga.entity.privilege.ResourceType;
import lombok.Data;

@Data
public class RangerResPri {
    private Long id;
    private String name;
    private String team;
    private ResourceType resourceType;
    private String resource;
    private Integer createTime;
    private Boolean isTrash;
}
