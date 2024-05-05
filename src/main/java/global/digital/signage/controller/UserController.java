package global.digital.signage.controller;

import global.digital.signage.model.user.User;
import global.digital.signage.payload.response.generic.DigitalSignageResponse;
import global.digital.signage.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/user")
public class UserController {
    private final UserService userService;

    @PutMapping("/role")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT_USER')")
    public ResponseEntity<DigitalSignageResponse<User>> updateUserRole(@RequestParam Long userId, @RequestParam Long roleId) {
        User response = userService.updateUserRole(userId, roleId);
        return ResponseEntity.ok(new DigitalSignageResponse<>(response));
    }
}
