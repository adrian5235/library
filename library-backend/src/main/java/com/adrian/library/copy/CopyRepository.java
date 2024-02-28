package com.adrian.library.copy;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CopyRepository extends JpaRepository<Copy, Integer> {

    List<Copy> findByAvailable(boolean available);

    List<Copy> findByEditionId(Integer editionId);
}
