package com.adrian.library.edition;

import com.adrian.library.book.BookDTO;
import com.adrian.library.binding.Binding;
import com.adrian.library.language.Language;
import com.adrian.library.publisher.Publisher;
import com.adrian.library.storage.FileData;
import com.adrian.library.translator.Translator;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@RequiredArgsConstructor
public class EditionDTO {

    private final int id;
    private final String name;
    private final String isbn13;
    private final String releaseYear;
    private final Language language;
    private final Binding binding;
    private final int numberOfPages;
    private final String weight;
    private final String dimensions;
    private final int quantity;
    private final Publisher publisher;
    private final Translator translator;
    private final BookDTO book;
    private final FileData image;
}
