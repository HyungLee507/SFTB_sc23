package CDProject.vfmarket.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Builder
@Table(name = "USERS")
@AllArgsConstructor
public class User extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Member_id")
    private Long id;

    private String password;

    private String email;

    private String name;

    private Integer footSize;

    private String age;

    private Integer footBallSize;

    private String mobile;

    private String refreshToken;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Enumerated(EnumType.STRING)
    private SocialType socialType; // KAKAO, NAVER, GOOGLE

    private String socialId;

    private String city;

    @Enumerated(EnumType.STRING)
    private AccountStatus status;

    @OneToMany(mappedBy = "buyer")
    private List<Order> orderList = new ArrayList<>();

    @OneToMany(mappedBy = "user")
    private List<DeliveryAddress> address;

    @OneToMany(mappedBy = "user")
    private List<StyleShot> styleShots = new ArrayList<>();

    @OneToOne(mappedBy = "user")
    private Cart shoppingBaskets;

    public void authorizeUser() {
        this.role = Role.MEMBER;
    }

    public void setFootSize(Integer footSize) {
        this.footSize = footSize;
    }

    public void passwordEncode(PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(this.password);
    }

    public void updateNickname(String updateNickname) {
        this.name = updateNickname;
    }

    public void updateAge(String updateAge) {
        this.age = updateAge;
    }

    public void updateMobile(String updateMobile) {
        this.mobile = updateMobile;
    }

    public void updatePassword(String updatePassword, PasswordEncoder passwordEncoder) {
        this.password = passwordEncoder.encode(updatePassword);
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateRefreshToken(String updateRefreshToken) {
        this.refreshToken = updateRefreshToken;
    }

}
