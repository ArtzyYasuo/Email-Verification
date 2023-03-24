package com.example.login.AppUser;

import com.example.login.Registration.token.ConfirmationToken;
import com.example.login.Registration.token.ConfirmationTokenService;
import lombok.AllArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AppUserService implements UserDetailsService {

    private final static String USER_NOT_FOUND_MSG = "user with email %s not found";
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return userRepository.findUserByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(String.format(USER_NOT_FOUND_MSG, email)));
    }

    //ctr+b to go to method by clicking on it
    public String signUpUser(AppUser appUser) {
        boolean userExists = userRepository
                .findUserByEmail(appUser.getEmail())
                .isPresent();
        if (userExists) {
//            TODO check if attributes are the same and
//            TODO if email not confirmed send confirmation email
            throw new IllegalStateException("email already taken");
        }

        String encodedPassword = passwordEncoder.encode(appUser.getPassword());

        appUser.setPassword(encodedPassword);

        //ctr+alt+l to format all to clean all the code
        userRepository.save(appUser);
        String token = UUID.randomUUID().toString();

        ConfirmationToken confirmationToken = new ConfirmationToken(
                token,
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(15),
                appUser
        );
        confirmationTokenService.saveConfirmationToken(confirmationToken);
        //TODO: SEND EMAIL
        return token;
    }

    public int enableAppUser(String email){
        return userRepository.enableAppUser(email);
    }
}
