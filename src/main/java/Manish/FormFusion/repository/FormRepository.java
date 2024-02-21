package Manish.FormFusion.repository;

import Manish.FormFusion.entity.Form;
import Manish.FormFusion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Repository
@Transactional
public interface FormRepository extends JpaRepository<Form, Long> {

    Optional<Form> findByformId(Long id);

    List<Form> findAllByUserUserId(Long userId);

    List<Form> findByUser(User user);
}
