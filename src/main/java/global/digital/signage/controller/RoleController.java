package global.digital.signage.controller;

import global.digital.signage.model.role.Role;
import global.digital.signage.model.role.RoleService;
import global.digital.signage.payload.request.RoleCreationRequest;
import global.digital.signage.payload.request.RoleUpdateRequest;
import global.digital.signage.payload.response.generic.DigitalSignageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static global.digital.signage.util.UtilService.getCompanyIdFromSecurityContext;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/roles")
public class RoleController {
    private final RoleService roleService;

    @GetMapping
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT_USER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DigitalSignageResponse<List<?>>> getAllRoles() {
        var roles = roleService.getAllRolesByCompanyId(getCompanyIdFromSecurityContext());
        return ResponseEntity.ok(new DigitalSignageResponse<>(roles));
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT_USER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DigitalSignageResponse<Role>> createRole(@RequestBody RoleCreationRequest roleCreationRequest) {
        var role = roleService.createRole(roleCreationRequest);
        log.info("role: {}", role);

        return ResponseEntity.ok(new DigitalSignageResponse<>(role));
    }

    @PutMapping("/update")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT_USER')")
    public ResponseEntity<DigitalSignageResponse<Role>> updateRole(@RequestBody RoleUpdateRequest roleUpdateRequest) {
        var role = roleService.update(roleUpdateRequest);
        log.info("update role request: {}", role);

        return ResponseEntity.ok(new DigitalSignageResponse<>(role));
    }

    @DeleteMapping("/delete")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT_USER')")
    public ResponseEntity<DigitalSignageResponse<Boolean>> deleteRoleById(@RequestParam Long roleId) {
        boolean response = roleService.deleteById(roleId);
        log.info("delete role request: {}", roleId);

        return ResponseEntity.ok(new DigitalSignageResponse<>(response));
    }

    @DeleteMapping("/delete-ids")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT_USER')")
    public ResponseEntity<DigitalSignageResponse<Boolean>> deleteRoleByIdList(@RequestBody List<Long> roleIds) {
        boolean response = roleService.deleteAllByIdList(roleIds);
        log.info("delete role request: {}", roleIds);

        return ResponseEntity.ok(new DigitalSignageResponse<>(response));
    }
}