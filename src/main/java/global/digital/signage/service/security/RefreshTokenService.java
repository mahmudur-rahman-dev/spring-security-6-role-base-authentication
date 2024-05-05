package global.digital.signage.service.security;

import global.digital.signage.model.RefreshToken;
import global.digital.signage.model.user.User;
import global.digital.signage.payload.request.RefreshTokenRequest;
import global.digital.signage.payload.response.RefreshTokenResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseCookie;

import java.util.Optional;

public interface RefreshTokenService {

    RefreshToken createRefreshToken(Long userId);

//    RefreshToken createRefreshToken(User userMaster);

    RefreshToken verifyExpiration(RefreshToken token);
    Optional<RefreshToken> findByToken(String token);
//    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);
    ResponseCookie generateRefreshTokenCookie(String token);

    String getRefreshTokenFromCookies(HttpServletRequest request);

    void deleteByToken(String token);

    //    String getRefreshTokenFromCookies(HttpServletRequest request);
//    void deleteByToken(String token);
    ResponseCookie getCleanRefreshTokenCookie();

    RefreshTokenResponse generateNewToken(RefreshTokenRequest request);
}