package global.digital.signage.service;

import global.digital.signage.model.company.CompanyRepository;
import global.digital.signage.model.permission.Permission;
import global.digital.signage.model.permission.PermissionService;
import global.digital.signage.model.role.ERole;
import global.digital.signage.model.role.RoleService;
import global.digital.signage.model.user.User;
import global.digital.signage.model.user.UserRepository;
import global.digital.signage.payload.request.RegisterRequest;
import global.digital.signage.payload.request.UserRegistrationRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

import static global.digital.signage.util.UtilService.getCompanyIdFromSecurityContext;

@Component
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final PermissionService permissionService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;
    private final CompanyRepository companyRepository;

    @Override
    public User getUserById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public Optional<User> getUserByUsername(String username) {
        return userRepository.findByName(username);
    }

    @Override
    public List<Permission> getPermissions() {
        return permissionService.findAll();
    }

    @Override
    public User createUser(UserRegistrationRequest request) {
        if (userRepository.findByName(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        var companyIdFromSecurityContext = getCompanyIdFromSecurityContext();
        var role = roleService.findByIdAndCompanyId(request.getRoleId(), companyIdFromSecurityContext);

        if (Objects.isNull(role)) {
            role = roleService.findByName(ERole.ROLE_USER);
        }

        var newUser = User.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .company(companyRepository.findById(companyIdFromSecurityContext).orElseThrow(() -> new IllegalArgumentException("Company not found")))
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public User createAdminUser(RegisterRequest request) {
        if (userRepository.findByName(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("User already exists");
        }

        var role = roleService.findByName(ERole.ROLE_ROOT_USER);
        User newUser = User.builder()
                .name(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(role)
                .company(companyRepository.findById(request.getCompanyId()).orElseThrow(() -> new IllegalArgumentException("Company not found")))
                .build();
        return userRepository.save(newUser);
    }

    @Override
    public User findById(Long userId) {
        return userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

    @Override
    public User updateUserRole(Long userId, Long roleId) {
        if(!isUserBelongsToRequesterCompany(userId)) {
            throw new IllegalArgumentException("User not found");
        }

        var role = roleService.findByIdAndCompanyId(roleId, getCompanyIdFromSecurityContext());

        if (Objects.isNull(role)) {
            throw new IllegalArgumentException("Role not found");
        }

        var user = findById(userId);
        user.setRole(role);
        return user;
    }

    private boolean isUserBelongsToRequesterCompany(Long userId) {
        return userRepository.existsByIdAndCompany_CompanyId(userId, getCompanyIdFromSecurityContext());
    }
}
