package CDProject.vfmarket.repository;


import CDProject.vfmarket.domain.entity.Member;
import org.springframework.data.repository.Repository;

import java.util.Optional;

public interface MemberRepository extends Repository<Member, String> {
    Optional<Member> findById(String email);
    void deleteById(String email);
    void delete(Member member);
    void save(Member member);
}
