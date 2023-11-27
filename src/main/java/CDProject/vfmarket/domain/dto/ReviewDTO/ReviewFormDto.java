package CDProject.vfmarket.domain.dto.ReviewDTO;

import jakarta.persistence.Lob;
import lombok.Data;

@Data
public class ReviewFormDto {

    private Long itemId;

    private String name;

    private Double starRate;

    @Lob
    private String content;

//    private List<MultipartFile> images;
}
