package com.adrian.library.storage;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String uploadImage(MultipartFile file, Integer editionId) throws IOException;
}
