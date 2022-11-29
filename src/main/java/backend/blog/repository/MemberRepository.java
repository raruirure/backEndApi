package backend.blog.repository;

import backend.blog.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MemberRepository {

    /**
     * spring boot(spring data jpa)가 entityManager가 @PersistenceContext 외에도 @Autowired를 통해서도 주입되게 처리해줘서
     * @RequiredArgsConstructor을 통한 final 생성자 처리를 가능하게 해줌
     */
    private final EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id); // type, PK
    }

    public List<Member> findAll() {
        return em.createQuery("select m from Member m", Member.class).getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class).setParameter("name", name).getResultList();
    }
}
