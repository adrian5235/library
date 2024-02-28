package com.adrian.library.language;

import java.util.List;

public interface LanguageService {
    List<Language> getAll();
    Language create(Language language);
    Language update(Language language);
}
