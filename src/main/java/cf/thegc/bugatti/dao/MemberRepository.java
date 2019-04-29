package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MemberRepository extends JpaRepository<Member, UUID> {

}
