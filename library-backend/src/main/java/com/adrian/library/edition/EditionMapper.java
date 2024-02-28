package com.adrian.library.edition;

import com.adrian.library.book.Book;
import com.adrian.library.book.BookDTO;
import com.adrian.library.book.BookMapper;
import com.adrian.library.binding.Binding;
import com.adrian.library.language.Language;
import com.adrian.library.publisher.Publisher;
import com.adrian.library.storage.FileData;
import com.adrian.library.translator.Translator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class EditionMapper {

    @Autowired
    private BookMapper bookMapper;

    public EditionDTO toDto(Edition edition) {
        int id = edition.getId();
        String name = edition.getName();
        String isbn13 = edition.getIsbn13();
        String releaseYear = edition.getReleaseYear();
        Language language = edition.getLanguage();
        Binding binding = edition.getBinding();
        int numberOfPages = edition.getNumberOfPages();
        String weight = edition.getWeight();
        String dimensions = edition.getDimensions();
        int quantity = edition.getQuantity();
        Publisher publisher = edition.getPublisher();
        Translator translator = edition.getTranslator();
        BookDTO book = bookMapper.toDto(edition.getBook());
        FileData image = edition.getImage();

        return new EditionDTO(id, name, isbn13, releaseYear, language, binding,
                numberOfPages, weight, dimensions, quantity, publisher, translator, book, image);
    }

    public Edition toEntity(EditionDTO editionDTO) {
        int id = editionDTO.getId();
        String name = editionDTO.getName();
        String isbn13 = editionDTO.getIsbn13();
        String releaseYear = editionDTO.getReleaseYear();
        Language language = editionDTO.getLanguage();
        Binding binding = editionDTO.getBinding();
        int numberOfPages = editionDTO.getNumberOfPages();
        String weight = editionDTO.getWeight();
        String dimensions = editionDTO.getDimensions();
        int quantity = editionDTO.getQuantity();
        Publisher publisher = editionDTO.getPublisher();
        Translator translator = editionDTO.getTranslator();
        Book book = bookMapper.toEntity(editionDTO.getBook());
        FileData image = editionDTO.getImage();

        return new Edition(id, name, isbn13, releaseYear, language, binding,
                numberOfPages, weight, dimensions, quantity, publisher, translator, book, image);
    }
}
