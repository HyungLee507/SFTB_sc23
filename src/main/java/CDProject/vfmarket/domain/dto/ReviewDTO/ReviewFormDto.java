package CDProject.vfmarket.domain.dto.ReviewDTO;

import jakarta.persistence.Lob;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ReviewFormDto {

    private Long itemId;

    private String name;

    private Double starRate;

    @Lob
    private String description;

    private List<MultipartFile> images;
}
