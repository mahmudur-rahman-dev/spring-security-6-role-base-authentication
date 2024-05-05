package global.digital.signage.service.security;

import global.digital.signage.model.user.User;
import global.digital.signage.payload.request.AuthenticationRequest;
import global.digital.signage.payload.request.RegisterRequest;
import global.digital.signage.payload.request.UserRegistrationRequest;
import global.digital.signage.payload.response.AuthenticationResponse;
import global.digital.signage.payload.response.RegistrationResponse;
import global.digital.signage.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;


@Service
@Transactional
@RequiredArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;
    private final UserService userService;
    private final AuthenticationProvider authenticationProvider;

    @Override
    public RegistrationResponse adminRegistration(RegisterRequest request) {
        User user = userService.createAdminUser(request);

        return RegistrationResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .roles(user.getRole())
                .companyId(user.getCompany().getCompanyId())
                .build();
    }

    @Override
    public RegistrationResponse userRegistration(UserRegistrationRequest request) {
        User user = userService.createUser(request);

        return RegistrationResponse.builder()
                .userId(user.getId())
                .username(user.getUsername())
                .roles(user.getRole())
                .companyId(user.getCompany().getCompanyId())
                .build();
    }

    @Override
    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationProvider.authenticate(new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword()));

        var user = userService.getUserByUsername(request.getUsername())
                .orElseThrow(() -> new IllegalArgumentException("Invalid Email or password."));

        var roles = user.getAuthorities()
                .stream()
                .map(GrantedAuthority::getAuthority)
                .toList();

        var jwt = jwtService.generateToken(user);
        var refreshToken = refreshTokenService.createRefreshToken(user.getId());

        return AuthenticationResponse.builder()
                .accessToken(jwt)
                .roles(roles)
                .username(user.getUsername())
                .companyId(Objects.isNull(user.getCompany()) ? null : user.getCompany().getCompanyId())
                .userId(user.getId())
                .refreshToken(refreshToken.getToken())
                .build();
    }
}
