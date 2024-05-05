package global.digital.signage.controller;

import global.digital.signage.exception.TokenNotFoundException;
import global.digital.signage.model.user.User;
import global.digital.signage.payload.request.*;
import global.digital.signage.payload.response.AuthenticationResponse;
import global.digital.signage.payload.response.RefreshTokenResponse;
import global.digital.signage.payload.response.RegistrationResponse;
import global.digital.signage.payload.response.generic.DigitalSignageResponse;
import global.digital.signage.service.UserService;
import global.digital.signage.service.security.AuthenticationService;
import global.digital.signage.service.security.JwtService;
import global.digital.signage.service.security.RefreshTokenService;
import global.digital.signage.util.UtilService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/api/v1/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @PostMapping("/login")
    public ResponseEntity<DigitalSignageResponse<AuthenticationResponse>> login(@RequestBody AuthenticationRequest authenticationRequest) {
        var response = authenticationService.authenticate(authenticationRequest);
        var generatedCookies = constructCookies(response);

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, generatedCookies.getLeft())
                .header(HttpHeaders.SET_COOKIE, generatedCookies.getRight())
                .body(new DigitalSignageResponse<>(response));
    }

    @PostMapping("/admin-registration")
    @PreAuthorize("hasAuthority('ROLE_SUPER_ADMIN')")
    public ResponseEntity<DigitalSignageResponse<RegistrationResponse>> adminRegistration(@RequestBody @Validated RegisterRequest registerRequest) {
        var register = authenticationService.adminRegistration(registerRequest);
        log.info("admin register: {}", register);
        return ResponseEntity.ok(new DigitalSignageResponse<>(register));
    }

    @PostMapping("/user-registration")
    @PreAuthorize("hasAnyAuthority('ROLE_ROOT_USER', 'ROLE_SUPER_ADMIN')")
    public ResponseEntity<DigitalSignageResponse<RegistrationResponse>> userRegistration(@RequestBody @Validated UserRegistrationRequest registerRequest) {
        var register = authenticationService.userRegistration(registerRequest);
        log.info("user register: {}", register);

        return ResponseEntity.ok(new DigitalSignageResponse<>(register));
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<DigitalSignageResponse<RefreshTokenResponse>> refreshToken(@RequestBody RefreshTokenRequest request) {
        var response = refreshTokenService.generateNewToken(request);
        return ResponseEntity.ok(new DigitalSignageResponse<>(response));
    }

    @PostMapping("/logout")
    public ResponseEntity<DigitalSignageResponse<String>> logout(
            @RequestHeader(HttpHeaders.AUTHORIZATION) String refreshToken
    ) {
        if (refreshToken != null) {
            refreshTokenService.deleteByToken(refreshToken);

            var cookies = clearCookies();
            log.info("user logged out.............");
            return ResponseEntity.ok()
                    .header(HttpHeaders.SET_COOKIE, cookies.getLeft())
                    .header(HttpHeaders.SET_COOKIE, cookies.getRight())
                    .body(new DigitalSignageResponse<>("Logged out successfully"));
        }
        throw new TokenNotFoundException("Logged out  unsuccessful");
    }

    private Pair<String, String> constructCookies(AuthenticationResponse response) {
        var jwtCookie = jwtService.generateJwtCookie(response.getAccessToken()).toString();
        var refreshTokenCookie = refreshTokenService.generateRefreshTokenCookie(response.getRefreshToken()).toString();
        return Pair.of(jwtCookie, refreshTokenCookie);
    }

    private Pair<String, String> clearCookies() {
        var jwtCookie = jwtService.getCleanJwtCookie().toString();
        var refreshTokenCookie = refreshTokenService.getCleanRefreshTokenCookie().toString();
        return Pair.of(jwtCookie, refreshTokenCookie);
    }

    private User getCurrentUser() {
        return userService.getUserById(UtilService.getRequesterUserIdFromSecurityContext());
    }
}
