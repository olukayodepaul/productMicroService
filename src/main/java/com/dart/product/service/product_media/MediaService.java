package com.dart.product.service.product_media;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.dart.product.entity.product_media_model.MediaUploadResponse;
import com.dart.product.utilities.AppConfig;
import com.dart.product.utilities.CustomRuntimeException;
import com.dart.product.utilities.ErrorHandler;
import com.dart.product.utilities.UtilitiesManager;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.UUID;

@Service
public class MediaService {

    private final UtilitiesManager utilitiesManager;

    public MediaService(UtilitiesManager utilitiesManager) {
        this.utilitiesManager = utilitiesManager;
    }

    // Directory where files will be saved
    private final String storageDirectory = "/Users/upload";

    @Async
    public MediaUploadResponse uploadFile(MultipartFile file) throws IOException {
        // Ensure the storage directory exists
        Path storagePath = Paths.get(storageDirectory);

        if (!Files.exists(storagePath)) {
            Files.createDirectories(storagePath);
        }

        // Generate a JWT-like unique name for the file
        String uniqueName = generateJwtLikeName();

        // Keep the original file extension
        String originalFilename = file.getOriginalFilename();
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1); // Get extension without dot
        }
        String fileName = uniqueName + "." + extension; // Ensure filename has extension

        // Define the full path where the file will be stored
        Path filePath = storagePath.resolve(fileName);

        // Check the media type
        String mediaType;
        if (utilitiesManager.isImageFile(extension)) {
            // Handle image file upload and resizing
            uploadAndResizeImage(file, filePath);
            mediaType = "image"; // Return media type without extension
        } else if (utilitiesManager.isVideoFile(extension)) {
            // Handle video file upload
            uploadVideo(file, filePath);
            mediaType = "video"; // Return media type without extension
        } else {
            throw new IOException("Unsupported file type: " + extension);
        }

        // Return media type and filename
        return new MediaUploadResponse(extension, fileName, mediaType);
    }



    private void uploadAndResizeImage(MultipartFile file, Path filePath) throws IOException {
        // Save the file temporarily
        File tempFile = File.createTempFile("temp-", file.getOriginalFilename());
        file.transferTo(tempFile);

        // Load the image
        BufferedImage originalImage = ImageIO.read(tempFile);

        // Check dimensions
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();

        // Prevent images smaller than 1000x1000
        if (width < 1000 || height < 1000) {
            System.out.println("Please upload an image that is at least 1000x1000 pixels."); // Log or handle as per your requirements
            throw new IOException("Uploaded image is too small. Please upload at least 1000x1000 pixels.");
        }

        // Check the aspect ratio of the image
        double aspectRatio = (double) width / height;

        // Check if the aspect ratio is close to 1 (tolerance level of 0.1)
        if (aspectRatio < 0.9 || aspectRatio > 1.1) {
            // Notify the customer that the image should be almost a square
            System.out.println("Please upload an image that is almost a square."); // Log or handle as per your requirements
            // Optionally, throw an exception if you want to prevent the upload
            // throw new IOException("Uploaded image should be almost a square.");
        }

        // Save the original image to the specified file path
        ImageIO.write(originalImage, "PNG", filePath.toFile()); // Adjust the format as needed

        // Delete the temporary file
        tempFile.delete();
    }

    private BufferedImage cropToSquare(BufferedImage originalImage) {
        int width = originalImage.getWidth();
        int height = originalImage.getHeight();
        int cropSize = Math.min(width, height);

        // Crop to the center of the image
        int x = (width - cropSize) / 2;
        int y = (height - cropSize) / 2;

        return originalImage.getSubimage(x, y, cropSize, cropSize);
    }

    private void uploadVideo(MultipartFile file, Path filePath) throws IOException {
        // Save the video file to the server directly
        file.transferTo(filePath.toFile());
    }

    private String generateJwtLikeName() {
        // Sample secret (should be secure in production)
        String secret = "mySecretKey";

        // Create the token
        String token = JWT.create()
                .withClaim("uuid", UUID.randomUUID().toString())
                .withClaim("timestamp", new Date().getTime())
                .sign(Algorithm.HMAC256(secret));
        return token;
    }


    // Method to delete an image from the file system
    public boolean deleteImage(String fileName) {
        // Define the full path of the file
        Path filePath = Paths.get(storageDirectory, fileName);
        System.out.println("images deleted "+fileName);

        try {
            // Check if the file exists before attempting to delete
            if (Files.exists(filePath)) {
                Files.delete(filePath);
                System.out.println("File deleted successfully: " + fileName);
                return true;
            } else {
                System.out.println("File not found: " + fileName);
                return false;
            }
        } catch (IOException e) {
            // Log or handle the exception properly
            System.err.println("Error deleting file: " + e.getMessage());
            return false;
        }
    }

}
