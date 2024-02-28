package com.adrian.library.storage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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

    @GetMapping("/{fileName}")
    @PreAuthorize("hasAuthority('storage:download')")
    public ResponseEntity<?> downloadImage(@PathVariable String fileName) throws IOException {
        byte[] imageData = storageService.downloadImage(fileName);
        return ResponseEntity.status(HttpStatus.OK)
                .contentType(MediaType.valueOf("image/png"))
                .body(imageData);
    }
}
