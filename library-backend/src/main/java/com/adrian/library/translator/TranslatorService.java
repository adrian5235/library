package com.adrian.library.translator;

import java.util.List;

public interface TranslatorService {
    List<Translator> getAll();
    Translator create(Translator translator);
    Translator update(Translator translator);
}
