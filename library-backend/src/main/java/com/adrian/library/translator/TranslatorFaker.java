package com.adrian.library.translator;

import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class TranslatorFaker {

    @Autowired
    TranslatorRepository translatorRepository;
    private final Faker faker = new Faker();

    public void saveTranslators(int n) {
        List<Translator> translators = translatorRepository.findAll();

        for (int i = 0; i < n; i++) {
            String name = faker.name().name();
            while (contains(translators, name)) {
                name = faker.name().name();
            }
            Translator translator = new Translator(name);
            translators.add(translator);
            translatorRepository.save(translator);
        }
    }

    // checks if translator with a given name exists in given list of translators
    private boolean contains(List<Translator> translators, String name) {
        for (Translator translator : translators) {
            if (translator.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
