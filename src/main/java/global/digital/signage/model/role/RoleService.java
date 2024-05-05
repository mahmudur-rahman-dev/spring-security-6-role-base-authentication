package global.digital.signage.model.role;

import global.digital.signage.payload.request.RoleCreationRequest;
import global.digital.signage.payload.request.RoleUpdateRequest;

import java.util.List;

public interface RoleService {

    Role findFirstByName(String name);

    Role update(RoleUpdateRequest roleUpdateRequest);

    void deleteByOrganization(Long organizationId);

    List<Role> getAllRoles();

    Role findById(Long id);

    Role findByIdAndCompanyId(Long id, Long companyId);

    Role createRole(RoleCreationRequest roleCreationRequest);

    List<Role> getAllRolesByCompanyId(Long companyIdFromClaims);

    Role findByName(ERole role);

    Role save(Role role);

    boolean deleteById(Long roleId);

    boolean deleteAllByIdList(List<Long> roleIds);
}
