package CDProject.vfmarket.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter@Setter
@NoArgsConstructor
public class Refund {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "orders_Id")
    private Order order;
    @OneToOne
    @JoinColumn(name = "item_Id")
    private Item item;
}
