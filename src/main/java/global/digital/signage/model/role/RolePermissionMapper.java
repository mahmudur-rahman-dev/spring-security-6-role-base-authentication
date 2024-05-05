package global.digital.signage.model.role;

import global.digital.signage.model.permission.Permission;
import global.digital.signage.model.permission.PermissionDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class RolePermissionMapper {

    public RoleDTO toRoleDTO(Role role) {
        if (role == null) {
            return null;
        }

        List<Permission> permissions = role.getPermissions();
        List<PermissionDTO> permissionDTOList = toPermissionDTOList(permissions);

        return RoleDTO.builder()
                .id(role.getRoleId())
                .name(role.getName())
                .note(role.getNote())
                .build();
    }

    public List<RoleDTO> toRoleDTOList(List<Role> roles) {
        if (roles != null && !roles.isEmpty()) {
            return roles.stream()
                    .map(this::toRoleDTO)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public RoleDTO toRoleDTOWithoutPermission(Role role) {
        if (role == null) {
            return null;
        }
        return RoleDTO.builder()
                .id(role.getRoleId())
                .name(role.getName())
                .build();
    }

    public List<RoleDTO> toRoleDTOListWithoutPermission(List<Role> roles) {
        if (roles != null && !roles.isEmpty()) {
            return roles.stream()
                    .map(this::toRoleDTOWithoutPermission)
                    .collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    public List<PermissionDTO> toPermissionDTOList(List<Permission> permissions) {
        if (permissions != null && !permissions.isEmpty()) {
            List<PermissionDTO> permissionDTOList = new ArrayList<>();
            permissions.forEach(p -> {
                if (p.getParent() == null) {
                    PermissionDTO parent = new PermissionDTO(p.getId(), p.getName());
                    List<Permission> children = new ArrayList<>();
                    permissions.forEach(p1 -> {
                        if (p1.getParent() != null && p1.getActive() && p1.getParent().equals(p.getId())) {
                            children.add(new Permission(p1.getId(), p1.getSort(), p1.getAction()));
                        }
                    });
                    parent.setChildren(children);
                    permissionDTOList.add(parent);
                }
            });
            return permissionDTOList;
        }
        return new ArrayList<>();
    }

    public StringBuilder toPermissionNames(List<Permission> permissions) {
        StringBuilder result = new StringBuilder();
        if (permissions != null && !permissions.isEmpty()) {
            Map<Long, List<Permission>> groupedPermissions = permissions.stream()
                    .collect(Collectors.groupingBy(permission -> permission.getParent() != null ? permission.getParent() : permission.getId()));

            groupedPermissions.forEach((parent, children) -> {
                result.append(children.get(0).getName());
                result.append(" (");
                result.append(children.stream().skip(1).map(Permission::getAction).collect(Collectors.joining(", ")));
                result.append("), ");
            });
        }
        return result;
    }
}
