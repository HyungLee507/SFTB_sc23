package CDProject.vfmarket.service;

import CDProject.vfmarket.domain.entity.Image;
import CDProject.vfmarket.repository.ImageRepository;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ImageService {

    private final ImageRepository imageRepository;

    @Value("${imagePath}")
    private String uploadFolder;

    private final Path rootLocation = Paths.get("C:/sw-capstone/images/");


    public void updateImage(Long imageId, MultipartFile file) throws IOException {
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid image ID: " + imageId));

        String uploadFileName = file.getOriginalFilename();
        String uuid = UUID.randomUUID().toString();
        uploadFileName = uuid + "_" + uploadFileName;
        File saveFile = new File(uploadFolder, uploadFileName);
        file.transferTo(saveFile);

        deleteExistingFile(image.getFileName());

        image.setFileName(uploadFileName);

    }

    public void deleteImage(Long imageId) throws IOException{
        Image image = imageRepository.findById(imageId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid image ID: " + imageId));

        imageRepository.delete(image);
    }

    private void deleteExistingFile(String fileName) {
        if (fileName != null && !fileName.isEmpty()) {
            try {
                Path file = rootLocation.resolve(fileName);
                Files.deleteIfExists(file);
            } catch (IOException e) {
                throw new RuntimeException("Failed to delete existing file: " + fileName, e);
            }
        }
    }
}
