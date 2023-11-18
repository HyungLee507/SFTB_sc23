package CDProject.vfmarket.domain.dto.itemDTO;

import jakarta.persistence.Lob;
import java.util.List;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ItemUpdateDto {

    private String name;

    @Lob
    private String description;

    private Integer shoeSize;

    private String category;

    private Long price;

    private List<MultipartFile> images;
}
