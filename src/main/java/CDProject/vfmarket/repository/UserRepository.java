package CDProject.vfmarket.repository;

import CDProject.vfmarket.domain.entity.SocialType;
import CDProject.vfmarket.domain.entity.User;
import java.util.Optional;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findById(Long Id);

    Optional<User> findByEmail(String email);

    Optional<User> findByName(String name);

    Optional<User> findByRefreshToken(String refreshToken);


    Optional<User> findBySocialTypeAndSocialId(SocialType socialType, String socialId);

    void deleteByEmail(String email);

//    @Modifying
//    @Transactional
//    @Query("update users ")
//    void updateToTempPassword();
}
