package CDProject.vfmarket.domain.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Lob;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "Comment_Id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @Lob
    private String description;

//    @Column(name = "orders")
//    private Integer order;

    @Enumerated(EnumType.STRING)
    private ReviewType reviewType;

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "Inquire_ID")
//    private Inquire inquire;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Review_ID")
    private Review review;


    // 부모 댓글 참조
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "Parent_Id")
    private Comment parent;

    // 자식 댓글 목록
    @OneToMany(mappedBy = "parent")
    private List<Comment> replies = new ArrayList<>();

//    @ManyToOne(fetch = FetchType.LAZY)
//    @JoinColumn(name = "item_ID")
//    private Item item;

//    @ManyToOne(fetch = FetchType.LAZY)
//    private Admin admin;
}
