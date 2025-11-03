package com.dev.job.service;

import com.dev.job.entity.resource.Image;
import com.dev.job.entity.user.User;
import com.dev.job.exceptions.BadRequestException;
import com.dev.job.repository.Resource.ImageRepository;
import com.dev.job.repository.User.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.experimental.NonFinal;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UploadService {

    final UserRepository userRepository;
    final ImageRepository imageRepository;

    @NonFinal
    @Value("${file.upload-dir}")
    String uploadDir;

    @Transactional
    public Image uploadUserAvatar(MultipartFile file, UUID userId) throws IOException {
        User user = getUser(userId);

        if(user.getAvatar() != null){
            File oldFile = new File(user.getAvatar().getFilePath());
            if(oldFile.exists()) {
                if(!oldFile.delete()){
                    throw new BadRequestException("Can not delete file");
                }
            };
            imageRepository.deleteById(user.getAvatar().getId());
        }

        String folder = uploadDir;

        Image image = saveImage(folder,"avatars", file);

        imageRepository.save(image);

        user.setAvatar(image);
        userRepository.save(user);

        return image;
    }

    @Transactional
    public Image uploadUserBackground(MultipartFile file, UUID userId) throws IOException {
        User user = getUser(userId);

        if(user.getCover() != null){
            File oldFile = new File(user.getCover().getFilePath());
            if(oldFile.exists() && !oldFile.delete()){
                throw new BadRequestException("Can not delete this file");
            }
            imageRepository.deleteById(user.getCover().getId());
        }

        String folder = uploadDir;

        Image image = saveImage(folder, "backgrounds" , file);

        imageRepository.save(image);
        user.setCover(image);
        userRepository.save(user);
        return image;
    }


    private User getUser(UUID userId){
        return  userRepository.findById(userId)
                .orElseThrow(() -> new BadRequestException("User not found."));
    }

    private Image saveImage(String folder, String sub, MultipartFile file) throws IOException {
        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.isEmpty()) {
            throw new BadRequestException("Invalid file name.");
        }

        String extension = FilenameUtils.getExtension(originalFileName);
        String fileName = UUID.randomUUID().toString() + "." + extension;

        Path uploadPath = Paths.get(folder, sub).toAbsolutePath().normalize();

        try {
            Files.createDirectories(uploadPath);
        } catch (IOException e) {
            throw new BadRequestException("Cannot create upload folder at: " + uploadPath);
        }

        Path targetPath = uploadPath.resolve(fileName);

        try {
            file.transferTo(targetPath.toFile());
        } catch (IOException e) {
            throw new IOException("Failed to save file: " + fileName, e);
        }

        Image image = new Image();
        image.setFileName(sub + "/" + fileName);
        image.setFilePath(targetPath.toString());
        image.setFileType(file.getContentType());
        image.setUploadedAt(LocalDateTime.now());

        return image;
    }


    @Transactional
    public List<Image> uploadJobImages(List<MultipartFile> files, UUID jobId) throws IOException {
        String folder = uploadDir;
        String sub = "jobs/" + jobId;

        List<Image> images = new ArrayList<>();

        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                Image image = saveImage(folder, sub ,file);
                imageRepository.save(image);
                images.add(image);
            }
        }

        return images;
    }

    public void deleteImages(List<Image> images) {
        System.out.println("Delete files.");
        for (Image image : images) {
            try {
                Path path = Paths.get(image.getFilePath());
                System.out.println("Deleting: " + path.toAbsolutePath());

                if (Files.exists(path)) {
                    Files.delete(path); // dùng Files.delete thay vì file.delete()
                    System.out.println("Deleted: " + path.getFileName());
                } else {
                    System.out.println("File not found: " + path.getFileName());
                }

                imageRepository.deleteById(image.getId());

            } catch (IOException e) {
                System.err.println("Failed to delete file: " + image.getFilePath());
                e.printStackTrace();
            }
        }
    }

    public void deletePostImageFolder(UUID postId) {
        Path postFolder = Paths.get(uploadDir, "posts", postId.toString());

        System.out.println("Deleting folder: " + postFolder.toAbsolutePath());

        try {
            if (Files.exists(postFolder)) {
                Files.walk(postFolder)
                        .sorted(Comparator.reverseOrder())
                        .forEach(path -> {
                            try {
                                Files.delete(path);
                                System.out.println("Deleted: " + path);
                            } catch (IOException e) {
                                System.err.println("Failed to delete: " + path);
                                e.printStackTrace();
                            }
                        });
            } else {
                System.out.println("Folder not found: " + postFolder);
            }
        } catch (IOException e) {
            System.err.println("Error while deleting folder: " + postFolder);
            e.printStackTrace();
        }
    }




}
