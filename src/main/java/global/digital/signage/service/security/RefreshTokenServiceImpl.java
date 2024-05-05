package global.digital.signage.service.security;


import global.digital.signage.enums.TokenType;
import global.digital.signage.enums.YNBoolean;
import global.digital.signage.exception.TokenException;
import global.digital.signage.exception.TokenNotFoundException;
import global.digital.signage.model.RefreshToken;
import global.digital.signage.model.user.UserRepository;
import global.digital.signage.payload.request.RefreshTokenRequest;
import global.digital.signage.payload.response.RefreshTokenResponse;
import global.digital.signage.repository.RefreshTokenRepository;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Service;
import org.springframework.web.util.WebUtils;

import java.time.Instant;
import java.util.Optional;


@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenServiceImpl implements RefreshTokenService {
    private final UserRepository userMasterRepository;
    private final RefreshTokenRepository refreshTokenRepository;
    private final JwtService jwtService;

    @Value("${application.security.jwt.refresh-token.expiration}")
    private long refreshExpiration;
    @Value("${application.security.jwt.refresh-token.cookie-name}")
    private String refreshTokenName;

    @Override
    public RefreshToken createRefreshToken(Long userId) {
        var user = userMasterRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        RefreshToken refreshToken = RefreshToken.builder()
                .revoked("N")
                .user(user)
                .token(jwtService.generateRefreshToken(user))
                .expiryDate(Instant.now().plusMillis(refreshExpiration))
                .build();
        log.info("refresh token generated.................");
        return refreshTokenRepository.save(refreshToken);
    }

//    @Override
//    public RefreshToken createRefreshToken(UserMaster userMaster) {
//        RefreshToken refreshToken = RefreshToken.builder()
//                .id(Integer.parseInt(refreshTokenSequenceGenerator.generateSequenceFromDB()))
//                .revoked(YNBoolean.N.name())
//                .user(userMaster)
//                .token(jwtService.generateRefreshToken(userMaster))
//                .expiryDate(Instant.now().plusMillis(refreshExpiration))
//                .build();
//
//        refreshToken.setRegisteredUserId(userMaster.getId());
//        refreshToken.setModifiedUserId(userMaster.getId());
//        log.info("refresh token generated.................");
//        return refreshTokenRepository.save(refreshToken);
//    }

    @Override
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token == null) {
            log.error("Token is null");
            throw new TokenNotFoundException("refresh token not found");
        }
        if (token.getExpiryDate().compareTo(Instant.now()) < 0) {
            refreshTokenRepository.delete(token);
            throw new TokenException(token.getToken(), "Refresh token was expired. Please make a new authentication request");
        }
        return token;
    }

    @Override
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

//    @Override
//    public RefreshTokenResponse generateNewToken(RefreshTokenRequest request) {
//        var refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken());
//        var user = refreshToken
//                .map(this::verifyExpiration)
//                .map(RefreshToken::getUser)
//                .orElseThrow(() -> new TokenNotFoundException(request.getRefreshToken(), "Refresh token does not exist"));
//
//        String token = jwtService.generateToken(user);
//        log.info("token generated using refresh token.................");
//
//        refreshToken.ifPresent(dbToken -> {
//            dbToken.setRevoked(YNBoolean.Y.name());
//            refreshTokenRepository.save(dbToken);
//        });
//
//        return RefreshTokenResponse.builder()
//                .accessToken(token)
//                .refreshToken(request.getRefreshToken())
//                .tokenType(TokenType.BEARER.name())
//                .build();
//    }

    @Override
    public ResponseCookie generateRefreshTokenCookie(String token) {
        return ResponseCookie.from(refreshTokenName, token)
                .path("/")
                .maxAge(refreshExpiration / 1000) // 15 days in seconds
                .httpOnly(true)
                .secure(true)
                .sameSite("Strict")
                .build();
    }

    @Override
    public String getRefreshTokenFromCookies(HttpServletRequest request) {
        Cookie cookie = WebUtils.getCookie(request, refreshTokenName);
        if (cookie != null) {
            return cookie.getValue();
        } else {
            return "";
        }
    }

    @Override
    public void deleteByToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresentOrElse(refreshTokenRepository::delete, () -> {
            throw new TokenNotFoundException("refresh token not found");
        });
    }

    @Override
    public ResponseCookie getCleanRefreshTokenCookie() {
        return ResponseCookie.from(refreshTokenName, "")
                .path("/")
                .maxAge(0) // Set Max-Age to 0
                .build();
    }

    @Override
    public RefreshTokenResponse generateNewToken(RefreshTokenRequest request) {
        var refreshToken = refreshTokenRepository.findByToken(request.getRefreshToken());
        var user = refreshToken
                .map(this::verifyExpiration)
                .map(RefreshToken::getUser)
                .orElseThrow(() -> new TokenNotFoundException(request.getRefreshToken(), "Refresh token does not exist"));

        String token = jwtService.generateToken(user);
        log.info("token generated using refresh token.................");

        refreshToken.ifPresent(dbToken -> {
            dbToken.setRevoked(YNBoolean.Y.name());
            refreshTokenRepository.save(dbToken);
        });

        return RefreshTokenResponse.builder()
                .accessToken(token)
                .refreshToken(request.getRefreshToken())
                .tokenType(TokenType.BEARER.name())
                .build();
    }
}