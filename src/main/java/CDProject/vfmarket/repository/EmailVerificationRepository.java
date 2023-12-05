package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.entity.EmailVerification;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface EmailVerificationRepository  extends JpaRepository<EmailVerification, Long> {

    List<EmailVerification> findByEmailOrderByIdDesc(String email);
}
