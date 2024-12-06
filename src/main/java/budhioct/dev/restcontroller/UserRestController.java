package budhioct.dev.restcontroller;

import budhioct.dev.security.user.UserDTO;
import budhioct.dev.security.user.UserService;
import budhioct.dev.utilities.Constants;
import budhioct.dev.utilities.RestResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class UserRestController {

    private final UserService userService;

    @PostMapping(
            path = "/register",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public ResponseEntity<RestResponse.object<UserDTO.RegisterResponse>> register(@RequestBody UserDTO.RegisterRequest request){
        UserDTO.RegisterResponse userResponse = userService.register(request);
        RestResponse.object<UserDTO.RegisterResponse> build = RestResponse.object.<UserDTO.RegisterResponse>builder()
                .data(userResponse)
                .status_code(Constants.CREATED)
                .message(Constants.REGISTER_MESSAGE)
                .build();
        return new ResponseEntity<>(build, HttpStatus.CREATED);
    }

    @PostMapping(
            path = "/login",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public RestResponse.object<UserDTO.LoginResponse> login(@RequestBody UserDTO.LoginRequest request) {
        UserDTO.LoginResponse loginResponse = userService.login(request);
        return RestResponse.object.<UserDTO.LoginResponse>builder()
                .data(loginResponse)
                .status_code(Constants.OK)
                .message(Constants.AUTH_LOGIN_MESSAGE)
                .build();
    }

}
