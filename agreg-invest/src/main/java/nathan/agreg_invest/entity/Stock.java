package nathan.agreg_invest.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Entity
@Table (name = "tb_stock")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @Column (name = "stock_id")
    private String stockId; //PETR4 MGLU4 IRBR3 ITSA4

    @Column (name = "description")
    private String description;

    private String ticker;

    public Stock(String stockId, String description) {
        this.stockId = stockId;
        this.description = description;
    }
}
