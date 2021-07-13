package imooc.naga.server.controller;

import cn.hutool.core.date.DateUtil;
import imooc.naga.core.util.JsonUtil;
import imooc.naga.entity.privilege.HdfsResource;
import imooc.naga.entity.privilege.HiveResource;
import imooc.naga.entity.privilege.ResourcePrivilege;
import imooc.naga.entity.privilege.ResourceType;
import imooc.naga.server.BaseController;
import imooc.naga.server.jwt.ContextUtil;
import imooc.naga.server.service.PrivilegeService;
import org.apache.parquet.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.*;

@RestController
@RequestMapping("/naga/v1/ranger")
@CrossOrigin
public class PrivilegeController extends BaseController {

    @Autowired
    PrivilegeService privilegeService;

    @GetMapping(value = "privileges")
    public Object getRangerPrivileges(
            @RequestParam(name = "pageIndex", required = true, defaultValue = "1") int pageIndex,
            @RequestParam(name = "pageSize", required = true, defaultValue = "50") int pageSize) throws IOException {

        Page<ResourcePrivilege> resourcePrivileges =
                privilegeService.listResourcePrivileges(ContextUtil.getCurrentUser().getTeam(), pageIndex-1, pageSize, null, null);
        List<RangerResPri> rangerResPris = new ArrayList<>();
        for(ResourcePrivilege resourcePrivilege:resourcePrivileges.getContent()){
            RangerResPri rangerResPri = new RangerResPri();
            rangerResPri.setId(resourcePrivilege.getId());
            rangerResPri.setCreateTime(resourcePrivilege.getCreateTime());
            rangerResPri.setIsTrash(resourcePrivilege.isTrash());
            rangerResPri.setName(resourcePrivilege.getName());
            rangerResPri.setResource(JsonUtil.toJson(resourcePrivilege.getResource()));
            rangerResPri.setResourceType(resourcePrivilege.getResourceType());
            rangerResPri.setTeam(resourcePrivilege.getTeam());
            rangerResPris.add(rangerResPri);
        }
        Map<String,Object> pages = new HashMap<>();
        pages.put("pages",rangerResPris);
        pages.put("pageIndex",pageIndex);
        pages.put("pageSize",pageSize);
        pages.put("pageCount",resourcePrivileges.getTotalPages());
        return getResult(pages);
    }

    @PostMapping(value = "privilege")
    public Object addRangerPrivilege(@RequestBody RangerResPri rangerResPri) throws IOException {
        ResourcePrivilege resourcePrivilege = new ResourcePrivilege();
        resourcePrivilege.setTrash(false);
        resourcePrivilege.setName(rangerResPri.getName());
        resourcePrivilege.setTeam(rangerResPri.getTeam());
        resourcePrivilege.setResourceType(rangerResPri.getResourceType());
        resourcePrivilege.setCreateTime(DateUtil.toIntSecond(new Date()));
        assert !Strings.isNullOrEmpty(rangerResPri.getResource());
        if(rangerResPri.getResourceType().equals(ResourceType.HDFS)){
            resourcePrivilege.setResource(JsonUtil.fromJson(HdfsResource.class,rangerResPri.getResource()));
        }else if(rangerResPri.getResourceType().equals(ResourceType.HIVE)){
            resourcePrivilege.setResource(JsonUtil.fromJson(HiveResource.class,rangerResPri.getResource()));
        }
        privilegeService.addPrivilege(resourcePrivilege);
        return getResult(true);
    }

    @PutMapping(value = "privilege")
    public Object updateRangerPrivilege(@RequestBody RangerResPri rangerResPri) throws IOException {
        ResourcePrivilege resourcePrivilege = new ResourcePrivilege();
        resourcePrivilege.setId(rangerResPri.getId());
        resourcePrivilege.setTrash(false);
        resourcePrivilege.setName(rangerResPri.getName());
        resourcePrivilege.setTeam(rangerResPri.getTeam());
        resourcePrivilege.setResourceType(rangerResPri.getResourceType());
        resourcePrivilege.setCreateTime(DateUtil.toIntSecond(new Date()));
        assert !Strings.isNullOrEmpty(rangerResPri.getResource());
        if(rangerResPri.getResourceType().equals(ResourceType.HDFS)){
            resourcePrivilege.setResource(JsonUtil.fromJson(HdfsResource.class,rangerResPri.getResource()));
        }else if(rangerResPri.getResourceType().equals(ResourceType.HIVE)){
            resourcePrivilege.setResource(JsonUtil.fromJson(HiveResource.class,rangerResPri.getResource()));
        }
        privilegeService.updatePrivilege(resourcePrivilege);
        return getResult(true);
    }

    @DeleteMapping(value = "privilege")
    public Object deleteRangerPrivilege(@RequestParam long id) {
        privilegeService.delPrivilege(id);
        return getResult(true);
    }

}
