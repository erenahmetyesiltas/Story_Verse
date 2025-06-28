package com.sweproject.storyVerse.security.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import com.sweproject.storyVerse.entity.User;
import com.sweproject.storyVerse.exception.EmailAlreadyExistsException;
import com.sweproject.storyVerse.exception.IncorrectPasswordException;
import com.sweproject.storyVerse.exception.InvalidPasswordException;
import com.sweproject.storyVerse.exception.UserNotFoundException;
import com.sweproject.storyVerse.repository.UserRepository;
import com.sweproject.storyVerse.request.UserChangePasswordRequest;
import com.sweproject.storyVerse.security.config.JwtService;
import com.sweproject.storyVerse.security.passwordValidation.PasswordValidation;
import com.sweproject.storyVerse.security.token.TokenRepository;
import com.sweproject.storyVerse.security.token.TokenType;
import com.sweproject.storyVerse.security.token.Token;
import com.sweproject.storyVerse.security.user.Role;
import com.sweproject.storyVerse.service.AdminService;
import com.sweproject.storyVerse.service.AuthorService;
import com.sweproject.storyVerse.service.ContributorService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final TokenRepository tokenRepository;
    private final AuthenticationManager authenticationManager;
    private final PasswordValidation passwordValidation;
    private final AdminService adminService;
    private final ContributorService contributorService;
    private final AuthorService authorService;

    boolean isOK = false;
    public String register(RegisterRequest request){

        User userException = userRepository.findByEmail(request.getEmail()).orElse(null);

        if(userException != null){
            throw new EmailAlreadyExistsException("The email has already exists.");
        }else if (!(passwordValidation.isEnoughLength(request.getPassword())) || !(passwordValidation.isIncludeAnyNumber(request.getPassword()))) {
            throw new InvalidPasswordException(passwordValidation.isValidPassword(request.getPassword()));
        }


        if(request.getRole().equals(Role.ADMIN)){
            adminService.addAnAdmin(request);
            isOK = true;
        }else if(request.getRole().equals(Role.AUTHOR)){
            authorService.addAnAuthor(request);
            isOK = true;
        }else if(request.getRole().equals(Role.CONTRIBUTOR)){
            contributorService.addAContributor(request);
            isOK = true;
        }

        if(isOK){

            User user = userRepository.findByEmail(request.getEmail()).orElse(null);
            var jwtToken = jwtService.generateToken((UserDetails) user);
            var refreshToken = jwtService.generateRefreshToken((UserDetails) user);
            saveUserToken(user,jwtToken);

            String result = "access token: "+jwtToken+"\n";
            result+="refresh token: "+refreshToken;

            return result;
        }else{
            return "There is an error about entered information";
        }
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request){

        try{

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            var user = userRepository.findByEmail(request.getEmail()).orElseThrow();
            var jwtToken = jwtService.generateToken((UserDetails) user);
            var refreshToken = jwtService.generateRefreshToken((UserDetails) user);
            revokeAllUserTokens(user);
            saveUserToken(user, jwtToken);
            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .build();
        }catch (AuthenticationException exception){
            throw new IncorrectPasswordException("The password you entered is incorrect for this email.");
        }

    }

    private void saveUserToken(User user, String jwtToken) {
        var token = Token.builder()
                .user(user)
                .token(jwtToken)
                .tokenType(TokenType.BEARER)
                .expired(false)
                .revoked(false)
                .build();

        tokenRepository.save(token);
    }

    private void revokeAllUserTokens(User user) {
        var validUserTokens = tokenRepository.findAllValidTokenByUser(user.getId());

        if(validUserTokens.isEmpty())
            return;

        validUserTokens.forEach(token -> {
            token.setExpired(true);
            token.setRevoked(true);
        });
        tokenRepository.saveAll(validUserTokens);
    }

    public void refreshToken(
            HttpServletRequest request,
            HttpServletResponse response) throws IOException {
        final String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        final String refreshToken;
        final String userEmail;

        if(authHeader == null || !authHeader.startsWith("Bearer ")){
            return;
        }

        refreshToken = authHeader.substring(7);
        userEmail = jwtService.extractUsername(refreshToken);

        if(userEmail != null){
            var user = this.userRepository.findByEmail(userEmail).orElseThrow();

            if(jwtService.isTokenValid(refreshToken, (UserDetails) user)){
                var accessToken = jwtService.generateToken((UserDetails) user);
                revokeAllUserTokens(user);
                saveUserToken(user,accessToken);
                var authResponse = AuthenticationResponse.builder()
                        .refreshToken(refreshToken)
                        .accessToken(accessToken)
                        .build();

                new ObjectMapper().writeValue(response.getOutputStream(), authResponse);
            }
        }
    }

    public String changePassword(Long userId, UserChangePasswordRequest request) {
        User user = userRepository.findById(userId).orElseThrow(() -> new UserNotFoundException("There is no such an user."));

        if (!(passwordEncoder.matches(request.getOldPassword(), user.getPassword()))) {
            throw new IncorrectPasswordException("There is no such an password");
        } else if (request.getOldPassword().equals(request.getNewPassword())) {
            throw new InvalidPasswordException("The new password cannot be same with beforehand.");
        } else if (!request.getNewPassword().equals(request.getVerifyPassword())) {
            throw new InvalidPasswordException("Your verified password does not match with your new password.");
        } else if (!(passwordValidation.isEnoughLength(request.getNewPassword())) || !(passwordValidation.isIncludeAnyNumber(request.getNewPassword()))) {
            throw new InvalidPasswordException(passwordValidation.isValidPassword(request.getNewPassword()));
        }  else {
            user.setPassword(passwordEncoder.encode(request.getNewPassword()));
            userRepository.save(user);
            return "Your password was changed successfully!";
        }

    }
}
