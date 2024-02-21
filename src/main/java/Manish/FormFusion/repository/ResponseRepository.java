package Manish.FormFusion.repository;

import Manish.FormFusion.entity.Form;
import Manish.FormFusion.entity.Response;
import Manish.FormFusion.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
@Transactional
public interface ResponseRepository extends JpaRepository<Response,Long> {
    List<Response> findByFormAndUser(Form form, User user);
}
