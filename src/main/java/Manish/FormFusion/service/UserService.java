package Manish.FormFusion.service;

import Manish.FormFusion.entity.User;
import Manish.FormFusion.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<User> user = Optional.ofNullable(userRepository.findByEmail(email));
        return user.map(UserInfoDetails::new)
                .orElseThrow(() -> new UsernameNotFoundException("User Not Found" + email));
    }


    public User addUser(User user) {
        String rawPassword = user.getPassword();
        if (rawPassword == null) {
            throw new IllegalArgumentException("Error: Password cannot be null");
        }

        User existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser != null && existingUser.getVerified()) {
            throw new RuntimeException("User Already Registered");
        }

        String otp = generateOTP();
        System.out.println("OPT Generated ............");
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("USER_ROLES");
        user.setOtp(otp);

        User savedUser = userRepository.save(user);
        sendVerificationEmail(savedUser.getEmail(), otp);
        return savedUser;
    }


    public void verify(String email, String otp) {
        User users = userRepository.findByEmail(email);
        if (users == null) {
            throw new RuntimeException("User not found");
        } else if (users.getVerified()) {
            throw new RuntimeException("User is already verified");
        } else if (otp.equals(users.getOtp())) {
            users.setVerified(true);
            userRepository.save(users);
        } else {
            throw new RuntimeException("Internal Server error");
        }
    }


    private String generateOTP() {
        System.out.println("Generating OPT ..................");
        Random random = new Random();
        int otpValue = 100000 + random.nextInt(900000);
        return String.valueOf(otpValue);
    }

    private void sendVerificationEmail(String email, String otp) {
        String subject = "Email verification";
        String body = "Dear User,\n\n"
                + "Thank you for registering with our platform. To complete your registration process, "
                + "please verify your email address by entering the following OTP:\n\n"
                + "Email verification OTP: " + otp + "\n\n"
                + "If you didn't initiate this registration process, please ignore this email.\n\n\n"
                + "Regards,\n"
                + "Manish Kumar Choudhary\n"
                + "Form Fusion";
        System.out.println("AT last step..............");
        emailService.sendEmail(email, subject, body);
    }


//    public String addUser(User user){
//        String rawPassword = user.getPassword();
//        if (rawPassword != null) {
//            user.setPassword(passwordEncoder.encode(rawPassword));
//            user.setRole("USER_ROLES");
//            userRepository.save(user);
//            return "User Added Successfully";
//        } else {
//            return "Error: Password cannot be null";
//        }
//    }


    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    public User getUser(Long userId) {
        return userRepository.findById(userId).get();
    }

    public boolean isEmailAlreadyRegistered(String email) {
        return userRepository.existsByEmail(email);
    }

}
