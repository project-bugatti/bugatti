package cf.thegc.bugatti.dao;

import cf.thegc.bugatti.model.Member;
import cf.thegc.bugatti.model.Quote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface QuoteRepository extends JpaRepository<Quote, UUID> {

}
