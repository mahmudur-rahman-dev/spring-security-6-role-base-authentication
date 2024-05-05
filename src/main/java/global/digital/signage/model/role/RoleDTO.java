package global.digital.signage.model.role;

import com.fasterxml.jackson.annotation.JsonInclude;
import global.digital.signage.model.permission.Permission;
import global.digital.signage.model.permission.PermissionDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RoleDTO {

    private Long id;

    private String name;

    private String note;

    private List<Permission> permissions = new ArrayList<>();

    private List<String> permissionNameList = new ArrayList<>();


}