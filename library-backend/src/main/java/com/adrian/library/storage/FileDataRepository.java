package com.adrian.library.storage;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileDataRepository extends JpaRepository<FileData, Integer> {
    Optional<FileData> findByName(String fileName);
    Optional<FileData> findByEditionId(Integer editionId);
}
