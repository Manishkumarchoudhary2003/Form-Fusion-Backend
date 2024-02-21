package Manish.FormFusion.repository;

import Manish.FormFusion.entity.Options;
import Manish.FormFusion.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface OptionsRepository extends JpaRepository<Options, Long> {
    List<Options> findByQuestion(Question question);

    void deleteInBatch(Iterable<Options> options);
}
