package Manish.FormFusion.controller;

import Manish.FormFusion.entity.*;
import Manish.FormFusion.repository.*;
import Manish.FormFusion.service.UserService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/form")
public class FormController {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private OptionsRepository optionsRepository;

    @Autowired
    private ResponseRepository responseRepository;

    @PostMapping("/{userId}/create-form")
    public ResponseEntity<Form> createForm(@PathVariable Long userId, @RequestBody Form form) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            form.setUser(user);
            Form savedForm = formRepository.save(form);
            return ResponseEntity.ok(savedForm);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }


//    @PostMapping("/{userId}/create-form")
//    public ResponseEntity<String> createForm(@PathVariable Long userId, @RequestBody Form form) {
//        try {
////            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
////            String username = authentication.getName();
////            User user = userRepository.findByUsername(username);
//
////            if (userId != null && !userId.equals(user.getUserId())) {
////                throw new UsernameNotFoundException("Provided userId does not match the authenticated user");
////            }
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
//
//            form.setUser(user);
//            formRepository.save(form);
//            return ResponseEntity.ok("Form successfully created");
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
//        }
//    }


//      Original
//    @PostMapping("/{userId}/{formId}/set-link")
//    public ResponseEntity<String> setFormUrl(@PathVariable Long userId, @PathVariable Long formId) {
//        try {
//            User user = userRepository.findById(userId)
//                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
//            Form form = formRepository.findById(formId)
//                    .orElseThrow(() -> new EntityNotFoundException("Form not found with ID: " + formId));
//
//            String url = "http://localhost:8080/form/" + userId + "/" + formId;
//            form.setLink(url);
//            formRepository.save(form);
//            return ResponseEntity.ok("Successfully Set the form Link -> " + url);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error generating form URL");
//        }
//    }


    @PostMapping("/{userId}/{formId}/set-link")
    public String setFormUrl(@PathVariable Long userId, @PathVariable Long formId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));
            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with ID: " + formId));

//            String url = "http://localhost:3000/form/user/" + userId + "/form/" + formId;
            String url = "https://form-fusion.netlify.app/form/user/" + userId + "/form/" + formId;
            form.setLink(url);
            formRepository.save(form);
            if (form.getQuestions().isEmpty()) {
                formRepository.delete(form);
                return "Successfully Set the form Link -> " + url + " and deleted form because it had no questions";
            }

            return "Successfully Set the form Link -> " + url;
        } catch (EntityNotFoundException e) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, e.getMessage());
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error generating form URL");
        }
    }


    @PutMapping("/{userId}/{formId}/update-form")
    public ResponseEntity<String> updateForm(
            @PathVariable Long userId,
            @PathVariable Long formId,
            @RequestBody Form updatedForm) {
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

            Optional<Form> existingFormOptional = formRepository.findById(formId);

            if (((Optional<?>) existingFormOptional).isPresent()) {
                Form existingForm = existingFormOptional.get();

                if (!existingForm.getUser().equals(user)) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have permission to update this form.");
                }

                String existingFormLink = existingForm.getLink();
                existingForm.setTitle(updatedForm.getTitle());
                existingForm.setDescription(updatedForm.getDescription());
                existingForm.setLink(existingFormLink);

                formRepository.save(existingForm);
                return ResponseEntity.ok("Form successfully updated");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Form not found with id: " + formId);
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @DeleteMapping("/{userId}/{formId}/delete-form")
    public ResponseEntity<String> deleteFormById(@PathVariable Long userId, @PathVariable Long formId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with ID: " + formId));

            // Delete all questions related to the form
            List<Question> questions = questionRepository.findByForm(form);
            for (Question question : questions) {
                // Delete options related to each question
                List<Options> options = optionsRepository.findByQuestion(question);
                optionsRepository.deleteAll(options);
            }
            questionRepository.deleteAll(questions);

            // Delete the form itself
            formRepository.delete(form);

            return ResponseEntity.ok("Form with ID " + formId + " and its related questions and options have been deleted.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting the form and its related questions and options.");
        }
    }

    @DeleteMapping("/{userId}/delete-no-links")
    public ResponseEntity<String> deleteFormsNoLinksForUser(@PathVariable Long userId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with ID: " + userId));

            List<Form> forms = formRepository.findByUser(user);
            int deletedCount = 0;

            for (Form form : forms) {
                if (form.getLink() == null) {
                    formRepository.delete(form);
                    deletedCount++;
                }
            }

            return ResponseEntity.ok("Deleted " + deletedCount + " forms with no links.");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{userId}/all-forms")
    public ResponseEntity<String> getAllFormsForUser(@PathVariable Long userId) {
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

            List<Form> forms = formRepository.findByUser(user);
            return ResponseEntity.ok(forms.toString());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }


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




//    @PostMapping("/{userId}/create-form")
//    public String createForm(@PathVariable(name = "userId", required = false) Long userId,
//                             @RequestBody Form form) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String authenticatedUsername = authentication.getName();
//        User authenticatedUser = userRepository.findByUsername(authenticatedUsername);
//
//        if (userId != null) {
//            if (!userId.equals(authenticatedUser.getUserId())) {
//                throw new RuntimeException("Provided userId does not match the authenticated user's userId");
//            }
//        } else {
//            userId = authenticatedUser.getUserId();
//        }
//
//        Long finalUserId = userId;
//        User user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + finalUserId));
//
//        form.setUser(user);
//        formRepository.save(form);
//        return "Form successfully created for user";
//    }
//
//    @GetMapping("/{userId}/getForms")
//    public ResponseEntity<List<Form>> getAllFormsForUser(@PathVariable(name = "userId") Long userId) {
//        try {
//            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//            String authenticatedUsername = authentication.getName();
//            User authenticatedUser = userRepository.findByUsername(authenticatedUsername);
//
//            if (userId != null) {
//                if (!userId.equals(authenticatedUser.getUserId())) {
//                    throw new RuntimeException("Provided userId does not match the authenticated user's userId");
//                }
//            } else {
//                userId = authenticatedUser.getUserId();
//            }
//
//            Long finalUserId = userId;
//
//            User user = userRepository.findById(finalUserId)
//                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + finalUserId));
//
//            List<Form> forms = formRepository.findByUser(user);
//
//            return ResponseEntity.ok(forms);
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.emptyList());
//        } catch (RuntimeException e) {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Collections.emptyList());
//        }
//    }


//    @GetMapping("/{userId}/getForms")
//    public List<Form> getAllFormsForUser(@PathVariable(name = "userId") Long userId) {
//        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
//        String authenticatedUsername = authentication.getName();
//        User authenticatedUser = userRepository.findByUsername(authenticatedUsername);
//
//        if (userId != null) {
//            if (!userId.equals(authenticatedUser.getUserId())) {
//                throw new RuntimeException("Provided userId does not match the authenticated user's userId");
//            }
//        } else {
//            userId = authenticatedUser.getUserId();
//        }
//
//        Long finalUserId = userId;
//
//        User user = userRepository.findById(finalUserId)
//                .orElseThrow(() -> new RuntimeException("User not found with id: " + finalUserId));
//
//        List<Form> forms = formRepository.findByUser(user);
//        return forms;
//    }
}
