package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {
    Page<LimitedMember> getAllBy(Pageable pageable);
}
