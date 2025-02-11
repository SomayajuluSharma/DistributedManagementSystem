package dev.stunning.userservice.Controller;

import dev.stunning.userservice.Exceptions.UserDoesNotExistException;
import dev.stunning.userservice.Models.SessionStatus;
import dev.stunning.userservice.dtos.*;
import dev.stunning.userservice.Exceptions.UserAlreadyExsists;
import dev.stunning.userservice.Models.User;
import dev.stunning.userservice.Service.AuthService;
import dev.stunning.userservice.dtos.SignUpRequestDto;
import dev.stunning.userservice.dtos.UserDto;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.config.annotation.web.configurers.saml2.Saml2LogoutConfigurer;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody LogoutRequestDto request) {
        return authService.logout(request.getToken(), request.getUserId());
    }

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginRequestDto request) throws UserDoesNotExistException {
        return authService.login(request.getEmail(), request.getPassword());
        //return new ResponseEntity<>(userDto, HttpStatus.OK);

    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> signUp(@RequestBody SignUpRequestDto request) throws UserAlreadyExsists {
        UserDto userDto = authService.signUp(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(userDto, HttpStatus.OK);
    }
    @PostMapping("/validate")
    public ResponseEntity<ValidateTokenResponseDto> validateToken(@RequestBody ValidateTokenRequestDto request) {
        Optional<UserDto> userDto = authService.validate(request.getToken(), request.getUserId());
        if(userDto.isEmpty()){
            ValidateTokenResponseDto response = new ValidateTokenResponseDto();
            response.setSessionStatus(SessionStatus.INVALID);
            return new ResponseEntity<>(response, HttpStatus.OK);
        }
        ValidateTokenResponseDto response = new ValidateTokenResponseDto();
        response.setSessionStatus(SessionStatus.ACTIVE);
        response.setUserDto(userDto.get());
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

}
