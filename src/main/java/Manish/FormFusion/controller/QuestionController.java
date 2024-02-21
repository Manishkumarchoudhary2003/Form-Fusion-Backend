package Manish.FormFusion.controller;

import Manish.FormFusion.entity.Form;
import Manish.FormFusion.entity.Options;
import Manish.FormFusion.entity.Question;
import Manish.FormFusion.entity.User;
import Manish.FormFusion.repository.FormRepository;
import Manish.FormFusion.repository.QuestionRepository;
import Manish.FormFusion.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/question")
public class QuestionController {

    @Autowired
    private FormRepository formRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private UserRepository userRepository;

    @PostMapping("/{userId}/{formId}/create-question")
    public ResponseEntity<String> createQuestionForForm(@PathVariable Long userId,
                                                        @PathVariable Long formId,
                                                        @RequestBody Question question) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            question.setForm(form);

            List<Options> options = question.getOptions();
            if (options != null) {
                for (Options option : options) {
                    option.setQuestion(question);
                }
            }

            questionRepository.save(question);

            return ResponseEntity.status(HttpStatus.CREATED).body("Question successfully created for the form");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error creating the question");
        }
    }

    @PutMapping("/{userId}/{formId}/{questionId}/update-question")
    public ResponseEntity<String> updateQuestionForForm(@PathVariable Long userId,
                                                        @PathVariable Long formId,
                                                        @PathVariable Long questionId,
                                                        @RequestBody Question updatedQuestion) {

        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            Question existingQuestion = questionRepository.findById(questionId)
                    .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId));

            // Update question text and form
            existingQuestion.setText(updatedQuestion.getText());
            existingQuestion.setForm(form);

            // Update options
            List<Options> existingOptions = existingQuestion.getOptions();
            List<Options> updatedOptions = updatedQuestion.getOptions();

            if (existingOptions != null && updatedOptions != null) {
                // Remove existing options not present in the updated question
                existingOptions.retainAll(updatedOptions);

                // Add new or updated options
                for (Options updatedOption : updatedOptions) {
                    if (!existingOptions.contains(updatedOption)) {
                        updatedOption.setQuestion(existingQuestion);
                        existingOptions.add(updatedOption);
                    }
                }
            } else {
                // Handle the case where options are null in the request
                existingQuestion.setOptions(updatedOptions);
            }

            questionRepository.save(existingQuestion);

            return ResponseEntity.status(HttpStatus.OK).body("Question successfully updated for the form");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the question");
        }
    }


    @DeleteMapping("/{userId}/{formId}/{questionId}/delete-question")
    public ResponseEntity<String> deleteQuestionAndOptions(@PathVariable Long userId,
                                                           @PathVariable Long formId,
                                                           @PathVariable Long questionId) {
        try {
            // Check if user and form exist
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            // Check if the question exists and is associated with the form
            Question question = (Question) questionRepository.findByIdAndFormId(questionId, formId)
                    .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId + " for form: " + formId));

            // Delete the question and its associated options
            questionRepository.delete(question);
            List<Question> remainingQuestions = questionRepository.findByFormId(formId);
            if (remainingQuestions.isEmpty()) {
                // Delete the form if no questions remain
                formRepository.delete(form);
                return ResponseEntity.ok("Question and its options successfully deleted, and form deleted as it has no remaining questions");
            }

            return ResponseEntity.ok("Question and its options successfully deleted");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error deleting the question and its options");
        }
    }
//    @GetMapping("/{userId}/{formId}/all-questions")
//    public ResponseEntity<String> getAllQuestionsForFormAndUser(@PathVariable  Long userId, @PathVariable Long formId) {
//        try {
//            Optional<User> optionalUser = userRepository.findById(userId);
//            Optional<Form> optionalForm = formRepository.findById(formId);
//
//            if (optionalUser.isPresent() && optionalForm.isPresent()) {
//                User user = optionalUser.get();
//                Form form = optionalForm.get();
//
//                // Check if the form belongs to the specified user
//                if (!form.getUser().equals(user)) {
//                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
//                            .body("Unauthorized: This form does not belong to the specified user.");
//                }
//
//                List<Question> questions = questionRepository.findByForm(form);
//
//                if (questions.isEmpty()) {
//                    return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                            .body("No questions found for the specified form.");
//                }
//
//                return ResponseEntity.ok(questions.toString());
//            } else {
//                throw new EntityNotFoundException("User or form not found with provided IDs");
//            }
//        } catch (EntityNotFoundException e) {
//            return ResponseEntity.status(HttpStatus.NOT_FOUND)
//                    .body("Entity not found: " + e.getMessage());
//        } catch (Exception e) {
//            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
//                    .body("Internal Server Error: " + e.getMessage());
//        }
//    }




    @GetMapping("/{userId}/{formId}/{questionId}/get-question")
    public ResponseEntity<String> getQuestionById(
            @PathVariable Long userId,
            @PathVariable Long formId,
            @PathVariable Long questionId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            // Check if the form belongs to the specified user
            if (!form.getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access: The specified form does not belong to the user.");
            }

            Optional<Question> questionOptional = questionRepository.findById(questionId);

            if (questionOptional.isPresent()) {
                Question question = questionOptional.get();

                // Check if the question belongs to the specified form
                if (!question.getForm().equals(form)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access: The specified question does not belong to the form.");
                }

                return ResponseEntity.ok(question.toString());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found with id: " + questionId);
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Unable to retrieve the question.");
        }
    }

    @GetMapping("/{userId}/{formId}/{questionId}/get-options")
    public ResponseEntity<String> getOptionsForQuestion(
            @PathVariable Long userId,
            @PathVariable Long formId,
            @PathVariable Long questionId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            // Check if the form belongs to the specified user
            if (!form.getUser().equals(user)) {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access: The specified form does not belong to the user.");
            }

            Optional<Question> questionOptional = questionRepository.findById(questionId);

            if (questionOptional.isPresent()) {
                Question question = questionOptional.get();

                // Check if the question belongs to the specified form
                if (!question.getForm().equals(form)) {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized access: The specified question does not belong to the form.");
                }

                List<Options> options = question.getOptions();
                return ResponseEntity.ok(options.toString());
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Question not found with id: " + questionId);
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error: Unable to retrieve the options for the question.");
        }
    }


}
