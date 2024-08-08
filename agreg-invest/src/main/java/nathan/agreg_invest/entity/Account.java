package nathan.agreg_invest.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "tb_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Account {

    @Id
    @GeneratedValue (strategy = GenerationType.UUID)
    @Column (name = "account_id")
    private UUID accountId;

    @OneToOne(cascade = CascadeType.ALL, mappedBy = "account") //possivel solução para "null identifier"
    @PrimaryKeyJoinColumn
    private BillingAddress billingAddress;

    @ManyToOne
    @JoinColumn (name = "user_id")
    private User user;

    @Column(name = "description")
    private String description;

    @OneToMany(mappedBy = "account")
    private List<AccountStock> accountStocks;

    public Account(UUID accountId, User user, String description, ArrayList<AccountStock> accountStocks) {

    }

    public Account(UUID accountId, User user, String description, String description1) {
    }
}
