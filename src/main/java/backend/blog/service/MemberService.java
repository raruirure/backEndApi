package backend.blog.service;

import backend.blog.repository.MemberRepository;
import backend.blog.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true) // 조회성 서비스에 대해서는 readOnly 처리필요
@RequiredArgsConstructor        // final있는 field만 가지고 생성자 만들어줌
public class MemberService {

    private final MemberRepository memberRepository;

//    @RequiredArgsConstructor //사용으로 대체됨
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    // 회원 가입
    @Transactional
    public Long join(Member member) {
        // 중복 체크
        validateDuplicateMember(member);
        // join 처리
        memberRepository.save(member);

        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    // 회원 목록 조회(전체)
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    // 회원 조회(단건)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }
}
