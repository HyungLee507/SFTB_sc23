package CDProject.vfmarket.domain.entity;

import static jakarta.persistence.FetchType.LAZY;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DeliveryAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Delivery_Address_Id")
    private Long id;
    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "Recipient_Member_Id")
    private User user;
    //받는 사람 이름
    private String name;
    private String zipcode;
    private String address;
    private String address_detail;
    private String phoneNum;
    private String requirement;
    private String defaultChoose;
}
