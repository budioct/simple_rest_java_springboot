package budhioct.dev.security.user;

import budhioct.dev.security.role.Role;
import budhioct.dev.utilities.annotation.ValidRole;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class UserDTO {

    @Getter
    @Setter
    @Builder
    public static class RegisterResponse {
        private String email;
        //private String password;
        private Role role;
    }

    @Getter
    @Setter
    @Builder
    public static class RegisterRequest {
        @NotBlank
        @Email(regexp = "[a-z0-9._%+-]+@[a-z0-9.-]+\\.[a-z]{2,3}", flags = Pattern.Flag.CASE_INSENSITIVE)
        private String email;
        @NotBlank
        @Size(min = 8, max = 200)
        private String password;
        @NotBlank
        @ValidRole(enumClass = Role.class, message = "Invalid role")
        private String role;
    }

    @Getter
    @Setter
    @Builder
    public static class LoginResponse {
        @JsonProperty("expires_in_minutes")
        private long expiresIn;
        @JsonProperty("role_is")
        private Role role;
        @JsonProperty("access_token")
        private String accessToken;
        @JsonProperty("refresh_token")
        private String refreshToken;
    }

    @Getter
    @Setter
    @Builder
    public static class LoginRequest {
        @NotBlank
        private String email;
        @NotBlank
        private String password;
    }

    public static RegisterResponse toRegisterResponse(UserEntity user){
        return RegisterResponse.builder()
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }

}
