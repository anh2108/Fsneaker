package com.example.fsneaker.utils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Optional;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.Part;

public class ImageUtils {
    private static final Path IMAGE_DIR = Paths.get(ConstantUtils.IMAGE_PATH);

    public static Optional<String> upload(HttpServletRequest request) {
        Optional<String> imageName = Optional.empty();
        try {
            Part filePart = request.getPart("image");
            if (filePart != null && filePart.getSize() > 0 && filePart.getContentType().startsWith("image")) {
                if (filePart.getSize() != 0 && filePart.getContentType().startsWith("image")) {
                    if (!Files.exists(IMAGE_DIR)) {
                        Files.createDirectories(IMAGE_DIR);
                    }
                    Path targetLocation = Files.createTempFile(IMAGE_DIR, "img-", ".jpg");
                    try (InputStream fileContent = filePart.getInputStream()) {
                        Files.copy(fileContent, targetLocation, StandardCopyOption.REPLACE_EXISTING);
                    }
                    imageName = Optional.of(targetLocation.getFileName().toString());
                }
            } else {
                throw new IllegalArgumentException("File không hợp lệ");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return imageName;
    }

    public static void delete(String imageName) {
        Path imagePath = IMAGE_DIR.resolve(imageName).normalize();
        try {
            boolean result = Files.deleteIfExists(imagePath);
            if (result) {
                System.out.println("File is deleted: " + imageName);
            } else {
                System.out.println("Sorry, unable to delete the file: " + imageName);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
