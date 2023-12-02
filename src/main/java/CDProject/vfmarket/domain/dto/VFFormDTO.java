package CDProject.vfmarket.domain.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class VFFormDTO {
    private MultipartFile image;
    private String dirname;
}
