package mx.aplazo.credit.interest.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import mx.aplazo.credit.interest.model.Credit;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long>{

}
