package budhioct.dev.security.user;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.userdetails.UserDetails;

import java.io.IOException;

public interface UserService {

    UserDTO.RegisterResponse register(UserDTO.RegisterRequest request);
    UserDTO.LoginResponse login(UserDTO.LoginRequest request);
    UserDTO.LoginResponse refreshToken(HttpServletRequest request, HttpServletResponse response) throws IOException;
    void changePassword(UserDTO.ChangePasswordRequest request, UserDetails userDetails);
}
