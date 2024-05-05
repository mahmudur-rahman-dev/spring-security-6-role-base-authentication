package global.digital.signage.model.permission;

import global.digital.signage.mapper.PermissionMapper;
import global.digital.signage.model.role.RolePermissionMapper;
import global.digital.signage.payload.response.PermissionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PermissionServiceImpl implements PermissionService {

    private final PermissionRepository permissionRepository;
    private final RolePermissionMapper rolePermissionMapper;
    private final PermissionMapper permissionMapper;

    @Override
    public List<Permission> findAll() {
        return permissionRepository.findAll();
    }

    @Override
    public List<PermissionResponse> getAllPermissions() {
        List<Permission> permissions = permissionRepository.findAll();
        Map<Long, List<Permission>> groupedPermissions = permissions.stream()
                .filter(permission -> permission.getParent() != null)
                .collect(Collectors.groupingBy(Permission::getParent));

        return groupedPermissions.entrySet().stream()
                .map(entry -> {
                    PermissionResponse parentResponse = permissionMapper.permissionToPermissionResponse(
                            permissions.stream()
                                    .filter(permission -> Objects.equals(permission.getId(), entry.getKey()))
                                    .findFirst()
                                    .orElse(null)
                    );
                    if (parentResponse != null) {
                        List<PermissionResponse> children = entry.getValue().stream()
                                .map(permissionMapper::permissionToPermissionResponse)
                                .collect(Collectors.toList());
                        parentResponse.setChildren(children);
                    }
                    return parentResponse;
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    @Override
    public List<Permission> findByIdIn(List<Long> permissionArray) {
        List<Permission> childrenList = permissionRepository.findByIdInAndActiveAndParentNotNullOrderBySortAsc(permissionArray, true);

        Set<Long> parentIdList = new HashSet<>();
        childrenList.forEach(p -> parentIdList.add(p.getParent()));

        List<Permission> parentList = permissionRepository.findByIdInAndActiveOrderBySortAsc(parentIdList.stream().toList(), true);

        List<Permission> permissionList = new ArrayList<>();
        permissionList.addAll(parentList);
        permissionList.addAll(childrenList);
        return permissionList;
    }

    @Override
    public List<Permission> findAllById(List<Long> permissionIds) {
        return permissionRepository.findAllById(permissionIds);
    }
}
