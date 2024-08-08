package nathan.agreg_invest.repository;


import nathan.agreg_invest.entity.Account;
import nathan.agreg_invest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountRepository extends JpaRepository<Account, UUID> {
}
