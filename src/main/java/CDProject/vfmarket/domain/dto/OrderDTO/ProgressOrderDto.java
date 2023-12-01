package CDProject.vfmarket.domain.dto.OrderDTO;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProgressOrderDto {

    private Long orderId;

    //상품 이름
    private String name;

    private String image;

    private Long price;

    private Integer shoeSize;

    private String address;

    private LocalDateTime createdDate;

    public ProgressOrderDto(OrderViewDto orderViewDtos, String address) {

        this.orderId = orderViewDtos.getOrderId();
        this.name = orderViewDtos.getName();
        this.createdDate = orderViewDtos.getCreatedDate();
        this.image = orderViewDtos.getImage();
        this.price = orderViewDtos.getPrice();
        this.shoeSize = orderViewDtos.getShoeSize();
        this.address = address;
    }
}
