package com.adrian.library.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@CrossOrigin("http://localhost:8081")
@RequestMapping("/storage")
@PreAuthorize("hasAnyRole('LIBRARIAN', 'ADMIN')")
public class StorageController {

    private final StorageServiceImpl storageService;

    @PostMapping
    @PreAuthorize("hasAuthority('storage:upload')")
    public String uploadImage(@RequestParam(name = "image")MultipartFile file,
                              @RequestParam(name = "editionId", required = false)Integer editionId) throws IOException {
        return storageService.uploadImage(file, editionId);
    }
}
