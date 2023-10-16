package CDProject.vfmarket.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@Getter @Setter
@NoArgsConstructor
public class DeliveryAddress {
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Delivery_Address_Id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Recipient_Member_Id")
    private Member member;
    //받는 사람 이름
    private String name;
    private String zipcode;
    private String address;
    private String address_detail;
    private String phoneNum;
    private String requirement;
    private String defaultChoose;
}
