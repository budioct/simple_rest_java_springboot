package budhioct.dev.security.user;

import budhioct.dev.security.jwt.JwtService;
import budhioct.dev.security.token.TokenEntity;
import budhioct.dev.security.token.TokenRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LogoutService implements LogoutHandler {

    private final TokenRepository tokenRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;

    @Transactional
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        try {
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                SecurityContextHolder.clearContext();
                throw new RuntimeException("Forbidden, Bearer is null");
            }

            final String jwt_token = authHeader.substring(7);
            TokenEntity store_token = tokenRepository.findFirstByToken(jwt_token)
                    .orElseThrow(() -> new RuntimeException("Forbidden, Bearer token is not found"));

            if (store_token.isExpired() || store_token.isRevoked()) {
                SecurityContextHolder.clearContext();
                throw new RuntimeException("Forbidden, Bearer token is expired or revoked");
            }
            String user_email = jwtService.extractUsername(jwt_token);
            userRepository.findFirstByEmail(user_email)
                    .orElseThrow(() -> new RuntimeException("Forbidden, User is not found"));

            store_token.setExpired(true);
            store_token.setRevoked(true);
            tokenRepository.save(store_token);
            SecurityContextHolder.clearContext();

        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
    }
}
