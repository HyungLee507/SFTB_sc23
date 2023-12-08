package CDProject.vfmarket.domain.dto.itemDTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ImageUpdateForm {

    private Long itemId;

    private MultipartFile image;
}
