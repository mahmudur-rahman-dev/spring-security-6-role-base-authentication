package global.digital.signage.util.Security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public interface CustomUserDetailsService extends UserDetailsService {
    CustomUserDetails loadUserByUsername(String username) throws UsernameNotFoundException;
}
