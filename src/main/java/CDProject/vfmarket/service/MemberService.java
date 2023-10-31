package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.entity.Member;
import CDProject.vfmarket.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class MemberService {
    @Autowired
    private MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository){
        this.memberRepository = memberRepository;
    }

    @Transactional
    public void saveMember(Member member){
        memberRepository.save(member);
    }
}
