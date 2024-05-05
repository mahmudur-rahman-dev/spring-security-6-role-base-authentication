package global.digital.signage.model.role;

import global.digital.signage.model.permission.PermissionService;
import global.digital.signage.payload.request.RoleCreationRequest;
import global.digital.signage.payload.request.RoleUpdateRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

import static global.digital.signage.util.UtilService.getCompanyIdFromSecurityContext;

@Service
@Slf4j
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {
    private final RoleRepository roleRepository;
    private final PermissionService permissionService;

    @Override
    public void deleteByOrganization(Long organizationId) {
        log.info("=======================================================================");
        log.info("DELETING ROLES");
        log.info("=======================================================================");

        List<Long> roleIds = findRoleIdsByOrganizationId(organizationId);
        log.info("{} ROLES ARE GOING TO BE DELETED", roleIds);
        deleteAllByIdList(roleIds);
    }

    @Override
    public List<Role> getAllRoles() {
        return roleRepository.findAll();
    }

    @Override
    public Role findFirstByName(String name) {
        return roleRepository.findFirstByName(name);
    }

    @Override
    public Role update(RoleUpdateRequest roleUpdateRequest) {
        Role companyRole = findByIdAndCompanyId(roleUpdateRequest.getId(), getCompanyIdFromSecurityContext());

        if (companyRole != null) {
            companyRole.setName(roleUpdateRequest.getName());
            companyRole.setNote(roleUpdateRequest.getNote());
            companyRole.setPermissions(permissionService.findAllById(roleUpdateRequest.getPermissionIds()));
            return save(companyRole);
        }

        return null;
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id).orElse(null);
    }

    @Override
    public Role findByIdAndCompanyId(Long id, Long companyId) {
        return roleRepository.findByRoleIdAndCompanyId(id, companyId);
    }

    @Override
    public Role createRole(RoleCreationRequest roleCreationRequest) {
        Role role = new Role();
        role.setName(roleCreationRequest.getName());
        role.setNote(roleCreationRequest.getNote());
        role.setCompanyId(getCompanyIdFromSecurityContext() == null ? -1L : getCompanyIdFromSecurityContext());

        var permissions = permissionService.findAllById(roleCreationRequest.getPermissionIds());

        role.setPermissions(permissions);
        return save(role);
    }

    @Override
    public List<Role> getAllRolesByCompanyId(Long companyIdFromClaims) {
        return roleRepository.findAllByCompanyId(companyIdFromClaims, ERole.ROLE_USER.name());
    }

    @Override
    public Role findByName(ERole role) {
        return roleRepository.findFirstByName(role.name());
    }

    @Override
    public Role save(Role role) {
        return roleRepository.save(role);
    }

    @Override
    public boolean deleteById(Long roleId) {
        roleRepository.deleteById(roleId);
        return true;
    }

    @Override
    public boolean deleteAllByIdList(List<Long> roleIds) {
        roleRepository.deleteAllByIdInBatch(roleIds);
        return true;
    }

    private List<Long> findRoleIdsByOrganizationId(Long organizationId) {
        return roleRepository.findRoleIdsByOrganizationId(organizationId);
    }
}
