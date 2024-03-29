package Manish.FormFusion.controller;

import Manish.FormFusion.entity.AuthOTP;
import Manish.FormFusion.entity.AuthRequest;
import Manish.FormFusion.entity.User;
import Manish.FormFusion.filter.JwtService;
import Manish.FormFusion.repository.UserRepository;
import Manish.FormFusion.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@Validated
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtService jwtService;

    @GetMapping("/welcome")
    public String welcome() {
        return "Welcome to Form Fusion !!";
    }

    //        @PostMapping("/register")
//    public String register(@RequestBody User user) {
//        return userService.addUser(user);
//    }

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody User user) {
        if (userService.isEmailAlreadyRegistered(user.getEmail())) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email is already registered. Please choose a different email.");
        } else {
            userService.addUser(user);
            return ResponseEntity.ok("User registered successfully.");
        }
    }


    @PostMapping("/verify")
    public String verifyUser(@RequestBody AuthOTP authOTP) {
        try {
            userService.verify(authOTP.getEmail(), authOTP.getOtp());
            return "User verified successfully";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }


    @PostMapping("/login")
    public String login(@RequestBody AuthRequest authRequest) {
        // Check if the user is verified
        User user = userRepository.findByEmail(authRequest.getEmail());
        if (user == null) {
            throw new UsernameNotFoundException("User not found");
        } else if (!user.getVerified()) {
            throw new RuntimeException("Email not verified");
        }

        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(),
                authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return jwtService.generateToken(authRequest.getEmail());
        } else {
            throw new UsernameNotFoundException("Invalid credentials");
        }
    }

//    @PostMapping("/login")
//    public String login(@RequestBody AuthRequest authRequest) {
//        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getEmail(), authRequest.getPassword()));
//        if (authenticate.isAuthenticated()) {
//            return jwtService.generateToken(authRequest.getEmail());
//        } else {
//            throw new UsernameNotFoundException("Invalid user request");
//        }
//    }

}
