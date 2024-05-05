package global.digital.signage.config;


import global.digital.signage.model.user.User;
import global.digital.signage.model.user.UserRepository;
import global.digital.signage.util.Security.CustomUserDetails;
import global.digital.signage.util.Security.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@RequiredArgsConstructor
public class ApplicationSecurityConfig {
    private final UserRepository userMasterRepository;

    @Bean
    @Primary
    public CustomUserDetailsService userDetailsService() {
        return username -> {
            User user = userMasterRepository.findByName(username)
                    .orElseThrow(() -> new UsernameNotFoundException("User not found"));

            var authorities = user.getAuthorities();

            return CustomUserDetails
                    .builder()
                    .userId(user.getId())
                    .username(user.getName())
                    .password(user.getPassword())
                    .companyId(user.getCompany() == null? null : user.getCompany().getCompanyId())
                    .authorities(authorities)
                    .build();
        };
    }

    @Bean
    @Primary
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
