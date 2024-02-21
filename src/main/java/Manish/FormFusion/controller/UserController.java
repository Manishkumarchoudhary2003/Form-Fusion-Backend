package Manish.FormFusion.controller;


import Manish.FormFusion.entity.*;
import Manish.FormFusion.repository.*;
import Manish.FormFusion.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/user")
@Validated
public class UserController {
    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FormRepository formRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private OptionsRepository optionsRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @GetMapping("/all")
    public ResponseEntity<String> getAllUsers() {
        try {
            List<User> users = userRepository.findAll();
            return ResponseEntity.ok(users.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


    @GetMapping("/{userId}")
    public ResponseEntity<String> getUserById(@PathVariable Long userId) {
        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String username = authentication.getName();
//            User user = userRepository.findByUsername(username);

//            if (userId != null && !userId.equals(user.getUserId())) {
//                throw new UsernameNotFoundException("Provided userId does not match the authenticated user");
//            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            return ResponseEntity.ok(user.toString());

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<String> getUserByEmail(@PathVariable String email) {
        try {
            User user = userRepository.findByEmail(email);
            if (user == null) {
                throw new EntityNotFoundException("User not found with email: " + email);
            }
            return ResponseEntity.ok(user.toString());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("An unexpected error occurred");
        }
    }

    @DeleteMapping("/{userId}/delete-user")
    public ResponseEntity<String> deleteUserById(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            List<Form> forms = formRepository.findByUser(user);

            for (Form form : forms) {
                // Delete responses related to each form
                List<Response> responses = responseRepository.findByFormAndUser(form, user);
                responseRepository.deleteAll(responses);
                List<Question> questions = questionRepository.findByForm(form);
                for (Question question : questions) {
                    // Delete options related to each question
                    List<Options> options = optionsRepository.findByQuestion(question);
                    optionsRepository.deleteAll(options);
                    List<Answer> answers = answerRepository.findByQuestionAndFormAndUser(question, form, user);
                    answerRepository.deleteAll(answers);
                }
                questionRepository.deleteAll(questions);
            }

            formRepository.deleteAll(forms);

            userRepository.delete(user);

            return ResponseEntity.ok("User with ID " + userId + " and all associated forms, questions, options, and answers have been deleted.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting the user and associated forms, questions, options, and answers.");
        }
    }


//    @GetMapping("/{userId}")
//    public ResponseEntity<User> getUserById(@PathVariable Long userId) {
//        try {
//            Optional<User> user = userRepository.findById(userId);
//            if (user.isPresent()) {
//                return ResponseEntity.ok(user.get());
//            } else {
//                return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
//            }
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
//        }
//    }


//    @GetMapping("/getUsers")
//    @PreAuthorize("hasAuthority('ADMIN_ROLES')")
//    public List<User> getAllUsers(){
//        return userService.getAllUsers();
//    }
//
//    @GetMapping("/getUsers/{id}")
//    @PreAuthorize("hasAuthority('USER_ROLES')")
//    public ResponseEntity<Object> getAllUsers(@PathVariable Long id) {
//        User user = userService.getUser(id);
//
//        if (user == null) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("User Not Found");
//        }
//
//        return ResponseEntity.ok(user);
//    }


//    @GetMapping("/getUsers/{id}")
//    @PreAuthorize("hasAuthority('USER_ROLES')")
//    public User getAllUsers(@PathVariable Long id){
//        System.out.println("Inside the getAllUsers");
//        return userService.getUser(id);
//    }

}
