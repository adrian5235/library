package com.adrian.library.translator;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslatorServiceImpl implements TranslatorService {

    private final TranslatorRepository repository;

    @Override
    public List<Translator> getAll() {
        return repository.findAll();
    }

    @Override
    public Translator create(Translator translator) {
        return repository.save(translator);
    }

    @Override
    public Translator update(Translator translator) {
        return repository.save(translator);
    }
}
