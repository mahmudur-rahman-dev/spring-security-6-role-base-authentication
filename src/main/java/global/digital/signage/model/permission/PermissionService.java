package global.digital.signage.model.permission;

import global.digital.signage.payload.response.PermissionResponse;

import java.util.List;

public interface PermissionService {

    List<Permission> findAll();

    List<PermissionResponse> getAllPermissions();

    List<Permission> findByIdIn(List<Long> permissionArray);

    List<Permission> findAllById(List<Long> permissionIds);
}
