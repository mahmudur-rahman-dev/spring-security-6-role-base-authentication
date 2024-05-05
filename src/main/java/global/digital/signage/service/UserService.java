package global.digital.signage.service;

import global.digital.signage.model.permission.Permission;
import global.digital.signage.model.user.User;
import global.digital.signage.payload.request.RegisterRequest;
import global.digital.signage.payload.request.UserRegistrationRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {
    User getUserById(Long userId);

    Optional<User> getUserByUsername(String username);

    List<Permission> getPermissions();

    User createUser(UserRegistrationRequest request);

    User createAdminUser(RegisterRequest request);

    User findById(Long userId);

    User updateUserRole(Long userId, Long roleId);
}
