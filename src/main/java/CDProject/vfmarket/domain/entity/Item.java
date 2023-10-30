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
public class Item extends BaseTimeEntity {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Item_id")
    private Long id;
    private String itemName;
    private Long price;
    private Integer showSize;

    private Long sellerId;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    //상품 대표 이미지인데... 이렇게 리스트 말고 정면 측면 후면 이렇게 나눠야 될지 고민...
    private String image1;
    private String image2;
    private String image3;
    private String image4;
    private String image5;
    @Lob
    private String description;

    private String category;

    @OneToOne(mappedBy = "item")
    private Order order;

    @OneToOne(mappedBy = "item")
    private Refund refund;

    @ManyToOne(fetch = FetchType.LAZY)
    private Cart shoppingBasket;

    @OneToMany(mappedBy = "item")
    private List<Comment> comment = new ArrayList<>();

}
