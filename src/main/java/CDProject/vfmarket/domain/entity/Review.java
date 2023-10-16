package CDProject.vfmarket.domain.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter@Setter
@NoArgsConstructor
public class Review {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long itemId;
    private Long memberId;
    @Column(name="orders")
    private int order;
    private String detail;
    private String image1;
    private String image2;
    private String image3;
    @OneToMany(mappedBy = "review")
    private List<Comment> comments = new ArrayList<>();






}
