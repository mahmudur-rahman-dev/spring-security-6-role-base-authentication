package global.digital.signage.mapper;

import global.digital.signage.model.permission.Permission;
import global.digital.signage.model.permission.PermissionDTO;
import global.digital.signage.payload.response.PermissionResponse;
import org.mapstruct.Mapper;
import org.mapstruct.NullValuePropertyMappingStrategy;
import org.mapstruct.factory.Mappers;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Mapper(componentModel = "spring", nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
public interface PermissionMapper {
    PermissionMapper INSTANCE = Mappers.getMapper(PermissionMapper.class);

    PermissionDTO permissionToPermissionDTO(Permission permission);

    List<PermissionDTO> permissionsToPermissionDTOs(List<Permission> permissions);

    PermissionResponse permissionToPermissionResponse(Permission permission);

    List<PermissionResponse> permissionsToPermissionResponses(List<Permission> permissions);

    default PermissionResponse mapWithChildren(Permission permission, List<Permission> allPermissions) {
        PermissionResponse response = permissionToPermissionResponse(permission);
        List<PermissionResponse> children = allPermissions.stream()
                .filter(child -> Objects.equals(child.getParent(), permission.getId()))
                .map(child -> mapWithChildren(child, allPermissions))
                .collect(Collectors.toList());
        response.setChildren(children);
        return response;
    }
}
