package CDProject.vfmarket.domain.entity;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.util.ArrayList;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Item extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Item_id")
    private Long id;
    private String itemName;
    private Long price;
    private Integer shoeSize;

    private Long sellerId;

    private String sellerName;

    @Enumerated(EnumType.STRING)
    private ItemStatus status;

    @OneToMany(mappedBy = "item", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    //    private String image1;
//    private String image2;
//    private String image3;
//    private String image4;
//    private String image5;

    @Lob
    private String description;

    private String category;

    @OneToOne(mappedBy = "item")
    private Order order;

//    @OneToOne(mappedBy = "item")
//    private Refund refund;

    @ManyToOne(fetch = FetchType.LAZY)
    private User itemUploadUser;

    //    @ManyToOne(fetch = FetchType.LAZY)
//    private Cart shoppingBasket;
    @ManyToMany(mappedBy = "items")
    private List<Cart> shoppingCarts = new ArrayList<>();

    @OneToMany(mappedBy = "item")
    private List<Comment> comment = new ArrayList<>();

    public List<Image> getImages() {
        if (this.images == null) {
            this.images = new ArrayList<>();
        }
        return this.images;
    }

}
