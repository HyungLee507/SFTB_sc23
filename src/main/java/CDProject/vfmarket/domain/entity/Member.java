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
public class Member extends BaseTimeEntity{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Member_id")
    private Long id;
    private String password;
    private String email;
    private String nickname;
    private Integer footSize;
    private Integer footBallSize;
    private String phoneNum;
    @Enumerated(EnumType.STRING)
    private MemberRoll roll = MemberRoll.MEMBER;
    @Enumerated(EnumType.STRING)
    private AccountStatus status;
    @OneToMany(mappedBy = "buyer")
    private List<Order> orderList = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<DeliveryAddress> address;
    @OneToMany(mappedBy = "member")
    private List<StyleShot> styleShots = new ArrayList<>();
    @OneToMany(mappedBy = "member")
    private List<ShoppingBasket> shoppingBaskets = new ArrayList<>();


}
