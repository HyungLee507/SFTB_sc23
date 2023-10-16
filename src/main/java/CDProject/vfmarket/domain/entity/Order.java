package CDProject.vfmarket.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ORDERS")
@NoArgsConstructor
public class Order {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Orders_Id")
    private Long id;
    private Long paymentPrice;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Buyer_Id")
    private Member buyer;
    //OneToOne 추후 수정
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_Id")
    private Item item;
    @OneToOne(mappedBy = "order")
    private Refund refund;
    private Long sellerId;
    private String representativeImage;
    @Enumerated(EnumType.STRING)
    private OrderStatus status;

}
