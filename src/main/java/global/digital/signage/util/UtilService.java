package global.digital.signage.util;

import global.digital.signage.exception.CustomSecurityException;
import global.digital.signage.model.role.ERole;
import global.digital.signage.model.user.User;
import global.digital.signage.service.UserService;
import global.digital.signage.util.Security.CustomUserDetails;
import io.jsonwebtoken.Claims;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.context.SecurityContextHolder;

import java.util.Optional;

import static global.digital.signage.util.Constants.ApiMessages.ERROR_SECURITY_CONTEXT;

@Slf4j
public class UtilService {
    public static Long getRequesterUserIdFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }

        throw new CustomSecurityException(ERROR_SECURITY_CONTEXT);
    }

    public static Long getCompanyIdFromClaims(Claims claims) {
        Object companyId = claims.get("companyId");
        if (companyId instanceof Integer) {
            return ((Integer) companyId).longValue();
        } else if (companyId instanceof Long) {
            return (Long) companyId;
        } else {
            throw new IllegalArgumentException("Invalid companyId type: " + companyId.getClass().getName());
        }
    }

    public static boolean isRootUser(Claims claims, UserService userService) {
        var userId = (Long) claims.get("userId");
        Optional<User> currentUser = Optional.ofNullable(userService.findById(userId));

        return currentUser.filter(user -> user.getRole().getName().equals(ERole.ROLE_ROOT_USER.name()))
                .isPresent();
    }


    public static boolean isSuperUser(Claims claims, UserService userService) {
        var userId = getUserIdFromSecurityContext();
        Optional<User> currentUser = Optional.ofNullable(userService.findById(userId));

        return currentUser.filter(user -> user.getRole().getName().equals(ERole.ROLE_SUPER_ADMIN.name()))
                .isPresent();
    }

    public static Long getUserIdFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getUserId();
        }

        throw new CustomSecurityException(ERROR_SECURITY_CONTEXT);
    }

    public static Long getCompanyIdFromSecurityContext() {
        var authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication.getPrincipal() instanceof CustomUserDetails) {
            return ((CustomUserDetails) authentication.getPrincipal()).getCompanyId();
        }

        throw new CustomSecurityException(ERROR_SECURITY_CONTEXT);
    }
}
