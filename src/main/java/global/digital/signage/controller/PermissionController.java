package global.digital.signage.controller;

import global.digital.signage.model.permission.PermissionService;
import global.digital.signage.payload.response.PermissionResponse;
import global.digital.signage.payload.response.generic.DigitalSignageResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/permission")
public class PermissionController {
    private final PermissionService permissionService;

    @GetMapping("/")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT_USER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DigitalSignageResponse<List<PermissionResponse>>> getAllPermissions() {
        var permissions = permissionService.getAllPermissions();
        return ResponseEntity.ok(new DigitalSignageResponse<>(permissions));
    }
}
