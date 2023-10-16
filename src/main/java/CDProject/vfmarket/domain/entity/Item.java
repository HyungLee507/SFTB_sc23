package CDProject.vfmarket.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Item {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Item_id")
    private Long id;
    private String itemName;
    private Long price;
    private Integer showSize;
    //이부분 어떻게 해야 되지?? => 유저랑 아이템 간의 연관 관계를 가져야 되는데 구매자, 판매자를 어떻게 나눠야 될지 고민해야 될듯....
    private Long sellerId;
    @Enumerated(EnumType.STRING)
    private ItemStatus status;
    //상품 대표 이미지인데... 이렇게 리스트 말고 정면 측면 후면 이렇게 나눠야 될지 고민...
    private List<String> imageUrls = new ArrayList<>();
    private String description;
    @OneToOne(mappedBy = "item")
    private Order order;
    @OneToOne(mappedBy = "item")
    private Refund refund;
    @ManyToOne(fetch = FetchType.LAZY)
    private ShoppingBasket shoppingBasket;
    @OneToMany(mappedBy = "item")
    private List<Comment> comment = new ArrayList<>();
}
