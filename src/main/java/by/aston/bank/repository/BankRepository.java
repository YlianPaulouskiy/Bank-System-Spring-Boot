package by.aston.bank.repository;

import by.aston.bank.model.Bank;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BankRepository extends JpaRepository<Bank, Long> {

    @Override
    @EntityGraph(attributePaths = {"id"})
    Optional<Bank> findById(Long aLong);

}
