package CDProject.vfmarket.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Comment extends BaseTimeEntity{
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Comment_Id")
    private Long id;

    private Long memberId;

    private String description;
    @Column(name = "orders")
    private Integer order;

    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="Inquire_ID")
    private Inquire inquire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Review_ID")
    private Review review;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="item_ID")
    private Item item;

    @ManyToOne(fetch = FetchType.LAZY)
    private Admin admin;
}
