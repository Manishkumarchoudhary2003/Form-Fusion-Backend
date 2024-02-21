package Manish.FormFusion.controller;

import Manish.FormFusion.entity.Answer;
import Manish.FormFusion.entity.Form;
import Manish.FormFusion.entity.Question;
import Manish.FormFusion.entity.User;
import Manish.FormFusion.repository.AnswerRepository;
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
@RequestMapping("answer")
public class AnswerController {

    @Autowired
    private FormRepository formRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private QuestionRepository questionRepository;

    @Autowired
    private AnswerRepository answerRepository;

    @PostMapping("/{userId}/{formId}/{questionId}/submit-answer")
    public ResponseEntity<String> submitAnswerForQuestion(
            @PathVariable Long userId,
            @PathVariable Long formId,
            @PathVariable Long questionId,
            @RequestBody Answer answer) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId));

            if (!form.getQuestions().contains(question)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Question does not belong to the specified form.");
            }

            // Check if an answer already exists for the question in the specified form
//            Optional<Answer> existingAnswer = answerRepository.findByQuestionAndFormAndUser(question, form, user);
//            if (existingAnswer.isPresent()) {
//                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("An answer already exists for the question in the specified form.");
//            }

            Answer submittedAnswer = new Answer(question, user, form, answer.getAnswer());
            answerRepository.save(submittedAnswer);

            return ResponseEntity.status(HttpStatus.CREATED).body("Answer successfully submitted for the question");
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error submitting the answer");
        }
    }


    @PutMapping("/{userId}/{formId}/{questionId}/{answerId}/update-answer")
    public ResponseEntity<String> updateAnswerForQuestion(
            @PathVariable Long userId,
            @PathVariable Long formId,
            @PathVariable Long questionId,
            @PathVariable Long answerId,
            @RequestBody Answer updatedAnswer) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId));

            if (!form.getQuestions().contains(question)) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Question does not belong to the specified form.");
            }

            // Find the specific answer to update
            Optional<Answer> existingAnswer = answerRepository.findById(answerId);

            if (existingAnswer.isPresent() && existingAnswer.get().getUser().equals(user)
                    && existingAnswer.get().getForm().equals(form) && existingAnswer.get().getQuestion().equals(question)) {
                Answer answerToUpdate = existingAnswer.get();
                answerToUpdate.setAnswer(updatedAnswer.getAnswer());
                answerRepository.save(answerToUpdate);

                return ResponseEntity.status(HttpStatus.OK).body("Answer successfully updated for the question");
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("No existing answer found for the specified answerId, question, form, and user.");
            }
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating the answer");
        }
    }


    @GetMapping("/{userId}/{formId}/{questionId}/get-answers")
    public ResponseEntity<String> fetchAnswers(@PathVariable Long userId, @PathVariable Long formId, @PathVariable Long questionId) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new EntityNotFoundException("User not found with id: " + userId));

            Form form = formRepository.findById(formId)
                    .orElseThrow(() -> new EntityNotFoundException("Form not found with id: " + formId));

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new EntityNotFoundException("Question not found with id: " + questionId));

            List<Answer> answers = answerRepository.findByQuestionAndFormAndUser(question, form, user);
            return ResponseEntity.ok(answers.toString());
        } catch (EntityNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
