package Manish.FormFusion.repository;


import Manish.FormFusion.entity.Form;
import Manish.FormFusion.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface QuestionRepository  extends JpaRepository<Question, Long> {
    List<Question> findByForm(Form form);

    @Query("SELECT q FROM Question q WHERE q.form.id = :formId AND q.id = :questionId")
    Optional<Question> findByIdAndFormId(@Param("questionId") Long questionId, @Param("formId") Long formId);

    @Query("SELECT q FROM Question q WHERE q.form.id = :formId")
    List<Question> findByFormId(@Param("formId") Long formId);}
