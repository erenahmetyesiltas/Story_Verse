package com.sweproject.storyVerse.security.auth;

import com.sweproject.storyVerse.exception.EmailAlreadyExistsException;
import com.sweproject.storyVerse.exception.IncorrectPasswordException;
import com.sweproject.storyVerse.exception.InvalidPasswordException;
import com.sweproject.storyVerse.exception.UserNotFoundException;
import com.sweproject.storyVerse.request.UserChangePasswordRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/v1/account")
@RequiredArgsConstructor
public class AuthenticationController {

    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request){
        return ResponseEntity.ok(authenticationService.register(request));
    }

    //public void register(@RequestBody RegisterRequest request){
//
    //    if(request.getRole().equals(Role.CUSTOMER)){
    //        customerService.addACustomer(request);
    //    }else if(request.getRole().equals(Role.OWNER)){
    //        ownerService.addOneOwner(request);
    //    }else if(request.getRole().equals(Role.ADMIN) || request.getRole().equals(Role.MANAGER)){
    //        managerAdminService.addOneManagerAdmin(request);
    //    }
    //}

    @PostMapping("/authenticate")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @PostMapping("/refresh-token")
    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response
    ) throws IOException {
        authenticationService.refreshToken(request,response);
    }

    @PostMapping("/{userId}/changePassword")
    public String changePassword(@PathVariable Long userId, @RequestBody UserChangePasswordRequest request){
        return authenticationService.changePassword(userId,request);
    }

    @ExceptionHandler(IncorrectPasswordException.class)
    public ResponseEntity<String> handleIncorrectPasswordException(IncorrectPasswordException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(EmailAlreadyExistsException.class)
    public ResponseEntity<String> handleEmailAlreadyExistsException(EmailAlreadyExistsException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<String> handleUserNotFoundException(UserNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPasswordException.class)
    public ResponseEntity<String> handleInvalidPasswordException(InvalidPasswordException exception){
        return new ResponseEntity<>(exception.getMessage(), HttpStatus.FORBIDDEN);
    }
}
