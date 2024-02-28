package com.adrian.library.language;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LanguageServiceImpl implements LanguageService {

    private final LanguageRepository repository;

    @Override
    public List<Language> getAll() {
        return repository.findAll();
    }

    @Override
    public Language create(Language language) {
        return repository.save(language);
    }

    @Override
    public Language update(Language language) {
        return repository.save(language);
    }
}