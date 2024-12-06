package budhioct.dev.security.user;

public interface UserService {

    UserDTO.RegisterResponse register(UserDTO.RegisterRequest request);

}
