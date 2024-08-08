package nathan.agreg_invest.repository;

import nathan.agreg_invest.entity.AccountStock;
import nathan.agreg_invest.entity.AccountStockId;
import nathan.agreg_invest.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface AccountStockRepository extends JpaRepository<AccountStock, AccountStockId> {
}
