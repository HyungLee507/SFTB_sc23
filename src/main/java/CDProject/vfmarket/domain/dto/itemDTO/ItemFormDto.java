package CDProject.vfmarket.domain.dto.itemDTO;

import jakarta.persistence.Lob;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemFormDto {

    private String name;
    private Integer shoeSize;
    private Long price;
    private String category;
    @Lob
    private String description;
    private List<MultipartFile> images;
}
