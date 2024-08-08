package nathan.agreg_invest.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table (name = "tb_accounts_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AccountStock {

    @EmbeddedId
    private AccountStockId accountStockId;

    @ManyToOne
    @MapsId("accountId")
    @JoinColumn (name = "account_id")
    private Account account;

    @ManyToOne
    @MapsId("stockId")
    @JoinColumn (name = "stock_id")
    private Stock stock;

    @Column (name = "quantity")
    private Integer quantity;
}
