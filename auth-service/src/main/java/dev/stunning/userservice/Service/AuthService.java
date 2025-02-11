package dev.stunning.userservice.Service;

import dev.stunning.userservice.Exceptions.UserAlreadyExsists;
import dev.stunning.userservice.Exceptions.UserDoesNotExistException;
import dev.stunning.userservice.Models.Session;
import dev.stunning.userservice.Models.SessionStatus;
import dev.stunning.userservice.Models.User;
import dev.stunning.userservice.Repositories.SessionRepository;
import dev.stunning.userservice.Repositories.UserRepository;
import dev.stunning.userservice.dtos.UserDto;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMapAdapter;

import java.util.HashMap;
import java.util.Optional;

@Service
public class AuthService {
    private PasswordEncoder passwordEncoder;
    public UserRepository userRepository;
    public SessionRepository sessionRepository;


    public AuthService(UserRepository userRepository, SessionRepository sessionRepository, PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.sessionRepository = sessionRepository;
       // this.bCryptPasswordEncoder = new BCryptPasswordEncoder();
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<UserDto> login(String email, String password) throws UserDoesNotExistException {
        Optional<User> userOptional = userRepository.findByEmail(email);
        if(userOptional.isEmpty()){
            throw new UserDoesNotExistException("User with email "+email+" does not exist");
        }
        User user = userOptional.get();
        if(!passwordEncoder.matches(password, user.getPassword())){
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
        String token = RandomStringUtils.randomAscii(20);
        MultiValueMapAdapter<String, String> headers = new MultiValueMapAdapter<>(new HashMap<>());
        headers.add("AUTH_TOKEN", token);

        Session session  = new Session();
        session.setSessionStatus(SessionStatus.ACTIVE);
        session.setToken(token);
        session.setUser(user);
        sessionRepository.save(session);

        UserDto userDto = UserDto.from(user);
        ResponseEntity<UserDto> response = new ResponseEntity<>(
                userDto,
                headers,
                HttpStatus.OK
        );
        return response;
    }

    public ResponseEntity<Void> logout(String token, Long userId) {
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);
        if (sessionOptional.isEmpty()) {
            return null;
        }
        Session session = sessionOptional.get();
        session.setSessionStatus(SessionStatus.LOGGED_OUT);
        sessionRepository.save(session);
        return ResponseEntity.ok().build();
    }

    public  UserDto signUp(String email, String password) throws UserAlreadyExsists {
        Optional<User> userOptional = userRepository.findByEmail(email);

        if(!userOptional.isEmpty()){
            throw new UserAlreadyExsists("User with email "+email+" already exists");
        }
        User user = new User();
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));

        User savedUser = userRepository.save(user);
        return UserDto.from(savedUser);
    }

    public Optional<UserDto> validate(String token, Long userId){
        Optional<Session> sessionOptional = sessionRepository.findByTokenAndUser_Id(token, userId);

        if(sessionOptional.isEmpty()){
            return Optional.empty();
        }
        Session session = sessionOptional.get();
        if(!session.getSessionStatus().equals(SessionStatus.ACTIVE)){
            return Optional.empty();
        }

        User user = userRepository.findById(userId).get();
        UserDto userDto = UserDto.from(user);
        return Optional.of(userDto);
    }
}
