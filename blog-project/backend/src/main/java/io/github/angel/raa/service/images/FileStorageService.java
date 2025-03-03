package io.github.angel.raa.service.images;

import io.github.angel.raa.excpetion.FileStorageException;
import org.springframework.web.multipart.MultipartFile;


public interface FileStorageService {
    String storeFile(MultipartFile file);
    void deleteFile(String fileName) throws FileStorageException;
    String getFileUrl(String fileName);

}
