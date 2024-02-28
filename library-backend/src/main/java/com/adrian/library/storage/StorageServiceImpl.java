package com.adrian.library.storage;

import com.adrian.library.edition.EditionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {

    private final FileDataRepository fileDataRepository;
    private final EditionRepository editionRepository;

    private static final String FOLDER_PATH = "D://Adrian//projects//library//library-frontend//src//assets//img";

    @Override
    public String uploadImage(MultipartFile file, Integer editionId) throws IOException {
        String filePath = FOLDER_PATH + "/";
        String fileName = "";

        FileData fileData = FileData.builder()
                .type(file.getContentType()).build();

        if (editionId != null) {
            fileName = "e" + editionId;
            Optional<FileData> existingFileData = fileDataRepository.findByEditionId(editionId);
            if (existingFileData.isPresent()) {
                fileData.setId(existingFileData.get().getId());
            }
            fileData.setEdition(editionRepository.getReferenceById(editionId));
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName != null) {
            String fileExtension = originalFileName.substring(originalFileName.length() - 4);
            fileName += fileExtension;
        }

        fileData.setName(fileName);
        filePath += fileName;
        fileData.setFilePath(filePath);
        fileDataRepository.save(fileData);
        file.transferTo(new File(filePath));

        return "File uploaded successfully: " + filePath;
    }

    @Override
    public byte[] downloadImage(String fileName) throws IOException {
        Optional<FileData> fileData = fileDataRepository.findByName(fileName);
        String filePath = fileData.get().getFilePath();
        return Files.readAllBytes(new File(filePath).toPath());
    }
}
