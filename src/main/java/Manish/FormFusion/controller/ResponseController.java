package Manish.FormFusion.controller;


import Manish.FormFusion.entity.Form;
import Manish.FormFusion.entity.Question;
import Manish.FormFusion.entity.Response;
import Manish.FormFusion.entity.User;
import Manish.FormFusion.repository.*;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("response")
public class ResponseController {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @GetMapping("/{userId}/{formId}/get-responses")
    public ResponseEntity<String> fetchResponsesByFormAndUser(@PathVariable Long userId, @PathVariable Long formId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            List<Response> responses = responseRepository.findByFormAndUser(form, user);
            return ResponseEntity.ok(responses.toString());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    @PostMapping("/{userId}/{formId}/send-response")
    public ResponseEntity<String> sendResponse(@PathVariable Long userId, @PathVariable Long formId) {

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("User not found"));

        Form form = formRepository.findById(formId)
                .orElseThrow(() -> new EntityNotFoundException("Form not found"));

        Response newResponse = new Response(form, user, LocalDateTime.now());
        responseRepository.save(newResponse);
        return ResponseEntity.status(HttpStatus.CREATED).body("Response submitted successfully for the Form id -> " + formId +
                " and User id ->" + userId);

    }

    @GetMapping("/{userId}/{formId}/all-questions")
    public ResponseEntity<String> getAllQuestionsForForm(
            @PathVariable Long userId,
            @PathVariable Long formId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            // Check if the form belongs to the specified user
            if (!form.getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: This form does not belong to the specified user.");
            }

            List<Question> questions = questionRepository.findByForm(form);

            if (questions.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("No questions found for the specified form.");
            }

            return ResponseEntity.ok((questions.toString()));
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Entity not found: " + e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: " + e.getMessage());
        }
    }

    @GetMapping("/{userId}/{formId}/getForm")
    public ResponseEntity<String> getFormById(
            @PathVariable Long userId,
            @PathVariable Long formId) {
        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String username = authentication.getName();
//            User user = userRepository.findByUsername(username);
//
//            if (userId != null && !userId.equals(user.getUserId())) {
//                throw new UsernameNotFoundException("Provided userId does not match the authenticated user");
//            }

            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            Optional<Form> formOptional = formRepository.findById(formId);

            if (formOptional.isPresent()) {
                Form form = formOptional.get();

                // Check if the authenticated user owns the requested form
                if (!form.getUser().equals(user)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to access this form.");
                }

                return ResponseEntity.ok(form.toString());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Form not found with id: " + formId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


}
