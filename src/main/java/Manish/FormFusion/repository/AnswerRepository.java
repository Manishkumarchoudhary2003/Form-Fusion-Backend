package Manish.FormFusion.repository;

import Manish.FormFusion.entity.Answer;
import Manish.FormFusion.entity.Form;
import Manish.FormFusion.entity.Question;
import Manish.FormFusion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface AnswerRepository extends JpaRepository<Answer, Long> {
    List<Answer> findByQuestionAndFormAndUser(Question question, Form form, User user);


}
